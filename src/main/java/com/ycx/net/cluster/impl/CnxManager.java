package com.ycx.net.cluster.impl;

import com.ycx.net.cluster.Vote;
import com.ycx.net.common.TangerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CnxManager {
    private static final Logger log = LoggerFactory.getLogger(CnxManager.class);

    static public final int maxBuffer = 2048;

    static final int RECV_CAPACITY = 100;

    static final int SEND_CAPACITY = 1;

    volatile boolean shutdown = false;

    private int connectTimeout = 5000;

    private final Listener listener;

    private final BlockingQueue<Vote> recvQueue;
    private final Object recvQLock = new Object();

    private final int myId;

    private final InetSocketAddress listenAddress;

    /**
     * 所有节点的地址信息 [id -> address]
     */
    private Map<Integer, InetSocketAddress> allNodes;

    // key: nodeId
    private final Map<Integer, SendWorker> sendWorkerMap;
    private final Map<Integer, BlockingQueue<ByteBuffer>> sendDataMap;
    private final Map<Integer, ByteBuffer> lastSendMsgMap;

    private final Set<Integer> inProgressConnections = Collections.synchronizedSet(new HashSet<>());

    private ThreadPoolExecutor connectionExecutor;


    public CnxManager(int myId, InetSocketAddress listenAddress, int connectThreadsSize) {
        this.listenAddress = listenAddress;
        this.recvQueue = new ArrayBlockingQueue<>(RECV_CAPACITY);
        this.sendWorkerMap = new ConcurrentHashMap<>(4);
        this.sendDataMap = new ConcurrentHashMap<>(4);
        this.lastSendMsgMap = new ConcurrentHashMap<>(4);

        this.myId = myId;

        final AtomicInteger threadIndex = new AtomicInteger(1);
        this.connectionExecutor = new ThreadPoolExecutor(3, connectThreadsSize, 60,
                TimeUnit.SECONDS, new SynchronousQueue<>(), (r) -> new TangerThread(r,
                String.format("ConnectionThread-[nodeId=%d]-%d", myId, threadIndex.getAndIncrement())));
        this.connectionExecutor.allowCoreThreadTimeOut(true);

        listener = new Listener("ListenerThread");
        listener.setDaemon(true);
        listener.start();
    }

    public static void closeSocket(Socket sock) {
        if (sock == null) {
            return;
        }

        try {
            sock.close();
        } catch (IOException ie) {
            log.error("Exception while closing", ie);
        }
    }

    private void setSockOpts(Socket sock) throws SocketException {
        sock.setTcpNoDelay(true);
//            sock.setKeepAlive(tcpKeepAlive);
//            sock.setSoTimeout(self.tickTime * self.syncLimit);
    }

    public SendWorker removeWorker(int nodeId) {
        return sendWorkerMap.remove(nodeId);
    }

    public BlockingQueue<ByteBuffer> getSendData(int nodeId) {
        return sendDataMap.get(nodeId);
    }

    public ByteBuffer getLastSendData(int nodeId) {
        return lastSendMsgMap.get(nodeId);
    }

    public void putLastSendData(int nodeId, ByteBuffer buffer) {
        lastSendMsgMap.put(nodeId, buffer);
    }

    public void recvVote(int nodeId, ByteBuffer buffer) {
        try {
            recvVote(new Vote(nodeId, buffer));
        } catch (Exception e) {
            log.error("parse vote error", e);
        }
    }

    public void recvVote(Vote vote) {
        synchronized (recvQLock) {
            if (recvQueue.remainingCapacity() == 0) {
                // if the queue is full, remove head
                try {
                    recvQueue.remove();
                } catch (NoSuchElementException e) {
                    log.debug("recvQueue is empty");
                }
            }
            recvQueue.add(vote);
        }
    }

    public Vote pollVote(long timeout, TimeUnit unit) throws InterruptedException {
        return recvQueue.poll(timeout, unit);
    }

    public void sendVote(Integer targetNode, Vote vote) {
        if (targetNode == myId) {
            recvVote(vote);
        } else {
            ByteBuffer buffer = vote.toByteBuffer();
            BlockingQueue<ByteBuffer> sendBuffers = sendDataMap.get(targetNode);
            if (sendBuffers == null) {
                sendBuffers = new ArrayBlockingQueue<>(SEND_CAPACITY);
            }
            if (sendBuffers.remainingCapacity() == 0) {
                try {
                    sendBuffers.remove();
                } catch (NoSuchElementException e) {
                    log.debug("sendQueue is empty");
                }
            }
            sendBuffers.add(buffer);
            // 确保建立连接
            connectTo(targetNode);
        }
    }

    private void connectTo(Integer nodeId) {
        if (sendWorkerMap.containsKey(nodeId)) {
            return;
        }
        InetSocketAddress address = allNodes.get(nodeId);
        if (address == null) {
            log.error("can not find target node {} address", nodeId);
            throw new RuntimeException("can not find target node " + nodeId + " address");
        }
        connectOne(nodeId, address);
    }


    class Listener extends TangerThread {
        private int maxRetry = 3;
        private Runnable socketBindErrorHandler = () -> System.exit(-1);
        volatile ServerSocket serverSocket;

        public Listener(String name) {
            super(name);
        }

        @Override
        public void run() {
            int numRetries = 0;
            Socket client = null;
            Exception exitException = null;

            while (!shutdown && numRetries < maxRetry) {
                log.debug("Listener thread started, myId: {}", myId);
                try {
                    serverSocket = new ServerSocket();
                    serverSocket.setReuseAddress(true);
                    serverSocket.bind(listenAddress);
                    log.info("{} is accepting connections now, my election bind port: {}", myId, listenAddress.getPort());
                    while (!shutdown) {
                        client = serverSocket.accept();
                        setSockOpts(client);
                        log.info("received connection from {}", client.getRemoteSocketAddress());
                        receiveConnection(client);
                        numRetries = 0;
                    }

                } catch (Exception e) {
                    numRetries++;
                    throw new RuntimeException(e);
                }
            }
        }

        private void receiveConnection(Socket client) {
            DataInputStream din;
            try {
                din = new DataInputStream(new BufferedInputStream(client.getInputStream()));
                // parse initial message
                InitialMessage msg = InitialMessage.parse(din);
                int nodeId = msg.getNodeId();

                if (nodeId < myId) {
                    // win the challenge
                    SendWorker sendWorker = sendWorkerMap.get(nodeId);
                    if (sendWorker != null) {
                        sendWorker.finish();
                    }

                    closeSocket(client);

                    connectOne(nodeId, msg.getAddress());
                } else if (nodeId == myId) {
                    log.error("received the same node id from {}", client.getRemoteSocketAddress());
                } else {
                    // keep connection
                    holdConnection(client, nodeId, din);
                }

            } catch (IOException e) {
                log.error("Exception while reading input stream from addr: {}", client.getRemoteSocketAddress());
                log.error("detail message: ", e);
                closeSocket(client);
            } catch (InitialMessage.InitialMessageException e) {
                log.error("Exception while parsing initial message", e);
                closeSocket(client);
            }
        }

        public void setSocketBindErrorHandler(Runnable errorHandler) {
            this.socketBindErrorHandler = errorHandler;
        }

    }

    private void holdConnection(Socket client, int nodeId, DataInputStream din) {
        SendWorker sendWorker = new SendWorker(client, nodeId, CnxManager.this);
        RecvWorker recvWorker = new RecvWorker(client, din, nodeId, sendWorker, CnxManager.this);
        sendWorker.setRecvWorker(recvWorker);

        SendWorker sw = sendWorkerMap.get(nodeId);
        if (sw != null) {
            sw.finish();
        }
        sendWorkerMap.put(nodeId, sendWorker);
        sendDataMap.putIfAbsent(nodeId, new ArrayBlockingQueue<>(1));

        sendWorker.start();
        recvWorker.start();
    }

    private class ConnectionReqTask implements Runnable {
        final InetSocketAddress electionAddr;
        final int nodeId;

        ConnectionReqTask(final InetSocketAddress electionAddr, final int nodeId) {
            this.electionAddr = electionAddr;
            this.nodeId = nodeId;
        }

        @Override
        public void run() {
            try {
                initiateConnection(electionAddr, nodeId);
            } finally {
                inProgressConnections.remove(nodeId);
            }
        }

        private void initiateConnection(InetSocketAddress electionAddr, int nodeId) {
            Socket sock = null;
            try {
                sock = new Socket();
                setSockOpts(sock);
                sock.connect(electionAddr, connectTimeout);
            } catch (Exception e) {
                log.warn("Cannot open channel to {} at election address {}", nodeId, electionAddr, e);
                closeSocket(sock);
                return;
            }

            try {
                startConnection(sock, nodeId);
            } catch (Exception e) {
                log.error(" start connection to {} at election address {} failed. ", nodeId, electionAddr, e);
                closeSocket(sock);
            }
        }
    }

    private void startConnection(Socket sock, int nodeId) {
        DataOutputStream dout;
        DataInputStream din;
        try {
            dout = new DataOutputStream(new BufferedOutputStream(sock.getOutputStream()));
            din = new DataInputStream(new BufferedInputStream(sock.getInputStream()));
            // send initial msg
            InitialMessage.writeMsg(dout, nodeId, listenAddress);
        } catch (IOException e) {
            closeSocket(sock);
            return;
        }

        if (nodeId > myId) {
            closeSocket(sock);
        } else {
            holdConnection(sock, nodeId, din);
        }
    }

    private void connectOne(int nodeId, InetSocketAddress address) {
        if (sendWorkerMap.containsKey(nodeId)) {
            log.error("Connection already exists for {}", nodeId);
            return;
        }

        if (!inProgressConnections.add(nodeId)) {
            log.debug("Connection request to server id: {} is already in progress", nodeId);
            return;
        }
        // submit connection task
        connectionExecutor.execute(new ConnectionReqTask(address, nodeId));
    }
}
