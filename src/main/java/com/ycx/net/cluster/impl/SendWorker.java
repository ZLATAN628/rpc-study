package com.ycx.net.cluster.impl;

import com.ycx.net.common.TangerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartboot.http.common.utils.CollectionUtils;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class SendWorker extends TangerThread {
    private static final Logger log = LoggerFactory.getLogger(SendWorker.class);
    private RecvWorker recvWorker = null;
    private final int nodeId;
    private final Socket socket;
    private final CnxManager cnxManager;
    private DataOutputStream dout;
    private volatile boolean running = true;

    public SendWorker(Socket socket, int nodeId, CnxManager cnxManager) {
        super(String.format("SendWorker-%s", nodeId));
        this.socket = socket;
        this.nodeId = nodeId;
        this.cnxManager = cnxManager;
        try {
            this.dout = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        } catch (IOException e) {
            log.error("Unable to access socket output stream", e);
            CnxManager.closeSocket(socket);
            running = false;
        }
    }

    public void setRecvWorker(RecvWorker recvWorker) {
        this.recvWorker = recvWorker;
    }

    public void finish() {
        if (!running) {
            return;
        }
        running = false;
        CnxManager.closeSocket(socket);
        this.interrupt();
        if (recvWorker != null) {
            recvWorker.finish();
        }
        cnxManager.removeWorker(nodeId);
    }

    synchronized void send(ByteBuffer buffer) throws IOException {
        byte[] bytes = new byte[buffer.capacity()];
        try {
            buffer.position(0);
            buffer.get(bytes);
        } catch (BufferUnderflowException be) {
            log.error("BufferUnderflowException ", be);
            return;
        }
        dout.writeInt(bytes.length);
        dout.write(bytes);
        dout.flush();
    }

    @Override
    public void run() {
        try {
            BlockingQueue<ByteBuffer> buffers = cnxManager.getSendData(nodeId);
            if (CollectionUtils.isEmpty(buffers)) {
                /**
                 * If there is nothing in the queue to send,
                 * send last vote
                 */
                ByteBuffer lastMsg = cnxManager.getLastSendData(nodeId);
                if (lastMsg != null) {
                    log.debug("Send lastMsg to node={}", nodeId);
                    send(lastMsg);
                }
            }
        } catch (IOException e) {
            log.error("Failed to send last msg to node={}", nodeId, e);
            this.finish();
            return;
        }

        try {
            while (running && socket != null) {
                try {
                    BlockingQueue<ByteBuffer> buffers = cnxManager.getSendData(nodeId);
                    if (buffers == null) {
                        log.error("no queue for node={}", nodeId);
                        break;
                    }
                    ByteBuffer buffer = buffers.poll(1000, TimeUnit.MILLISECONDS);
                    if (buffer != null) {
                        cnxManager.putLastSendData(nodeId, buffer);
                        send(buffer);
                    }
                } catch (InterruptedException e) {
                    log.warn("Interrupted while waiting for message on queue", e);
                }
            }
        } catch (Exception e) {
            log.error("send data to node={} error", nodeId, e);
        }
        this.finish();
    }
}
