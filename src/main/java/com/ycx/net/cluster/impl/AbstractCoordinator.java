package com.ycx.net.cluster.impl;

import com.ycx.net.cluster.*;
import com.ycx.net.common.TangerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public abstract class AbstractCoordinator implements Coordinator {

    private static final Logger log = LoggerFactory.getLogger(AbstractCoordinator.class);
    /**
     * 所有节点的选举地址信息 [id -> address]
     */
    protected Map<Integer, InetSocketAddress> allNodesElectionAddress;
    /**
     * 所有节点的 RPC 地址信息 [id -> address]
     */
    protected Map<Integer, InetSocketAddress> allNodesRpcAddress;

    /**
     * 连接管理器
     */
    protected CnxManager cnxManager;

    /**
     * 当前自己的投票
     */
    protected Vote currentVote;

    /**
     * 建议的 Leader
     */
    protected int proposedLeader;
    protected long proposedLeaderStartupTime;

    /**
     * 当前角色
     */
    protected volatile NodeRole currentRole;

    /**
     * 选举开始时间
     */
    protected long electionStartTime;

    /**
     * 当前 node ID
     */
    protected int myId;

    /**
     * 记录每个节点的投票信息
     */
    protected Map<Integer, Vote> votingMap;

    /**
     * 接受选票信息
     */
    private final WorkerReceiver workerReceiver;

    private final BlockingQueue<Vote> recvQueue;

    private final Set<RoleChangeListener> roleChangeListeners = new HashSet<>();

    InetSocketAddress listenAddress;

    /**
     * 如果当前节点是 Leader 节点，缓存所有子节点信息
     */
    private final HashMap<Integer, Node> nodeMap = new HashMap<>();

    public AbstractCoordinator(Integer nodeId, String allNodesAddress, Integer connectThreadsSize) {
        this.myId = nodeId;
        this.workerReceiver = new WorkerReceiver("workerReceiver");
        this.workerReceiver.setDaemon(true);
        this.recvQueue = new LinkedBlockingQueue<>();
        this.votingMap = new HashMap<>();
        parseNodeAddress(allNodesAddress);
        this.cnxManager = new CnxManager(nodeId, listenAddress, connectThreadsSize, allNodesElectionAddress);
    }

    private void parseNodeAddress(String allNodesAddress) {
        this.allNodesElectionAddress = new HashMap<>();
        this.allNodesRpcAddress = new HashMap<>();
        if (allNodesAddress == null || allNodesAddress.isEmpty()) {
            throw new IllegalArgumentException("allNodesAddress is null or empty");
        }
        // "server.1=172.16.142.208:8077:8076,server.2=172.16.140.41:8077:8076,server.3=172.16.145.239:8077:8076"
        String[] servers = allNodesAddress.split(",");
        for (String server : servers) {
            String[] split = server.split("=");
            if (split.length != 2) {
                throw new IllegalArgumentException("Invalid server address: " + server);
            }
            Integer nodeId = Integer.parseInt(split[0].replace("server.", ""));
            String[] ipAndPort = split[1].split(":");
            if (ipAndPort.length != 3) {
                throw new IllegalArgumentException("Invalid server address: " + server);
            }
            int rpcPort = Integer.parseInt(ipAndPort[1]);
            int electionPort = Integer.parseInt(ipAndPort[2]);
            InetSocketAddress rpcAddress = new InetSocketAddress(ipAndPort[0], rpcPort);
            InetSocketAddress electionAddress = new InetSocketAddress(ipAndPort[0], electionPort);
            this.allNodesRpcAddress.put(nodeId, rpcAddress);
            this.allNodesElectionAddress.put(nodeId, electionAddress);
            if (nodeId == this.myId) {
                this.listenAddress = electionAddress;
            }
        }
    }


    @Override
    public void startElection() {
        log.info("start election");
        if (this.workerReceiver.getState() == Thread.State.NEW) {
            this.workerReceiver.start();
        }
        initialSelfVote();
        // notify all nodes
        sendNotifications();
        electionStartTime = System.currentTimeMillis();
        while (currentRole == NodeRole.CANDIDATE) {
            try {
                Vote vote = recvQueue.poll(3000, TimeUnit.MILLISECONDS);
                if (vote == null) {
                    long current = System.currentTimeMillis();
                    // 没有收到数据 自己当 Leader
                    if (current - electionStartTime > 10000 && votingMap.size() == 1) {
                        currentRole = NodeRole.LEADER;
                        log.info("Received null vote become Leader");
                    }
                } else {
                    handleVoteRequest(vote);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        triggerListener();
        log.info("election end I am {} follow Leader {}", currentRole, proposedLeader);
    }

    /**
     * 初始化选票 投自己为 Leader
     */
    private void initialSelfVote() {
        proposedLeader = myId;
        currentRole = NodeRole.CANDIDATE;
        currentVote = new Vote(proposedLeader, currentRole, myId);
        proposedLeaderStartupTime = currentVote.getStartupTime();

    }

    protected void updateVote(Vote vote) {
        proposedLeader = vote.getLeader();
        proposedLeaderStartupTime = vote.getStartupTime();
        currentVote.setLeader(proposedLeader);
        currentVote.setStartupTime(proposedLeaderStartupTime);
    }

    private void sendNotifications() {
        for (Map.Entry<Integer, InetSocketAddress> entry : allNodesElectionAddress.entrySet()) {
            cnxManager.sendVote(entry.getKey(), currentVote);
        }
    }

    @Override
    public void handleVoteRequest(Vote vote) {
        try {
            NodeRole peerRole = vote.getRole();
            if (currentRole == NodeRole.CANDIDATE) {
                if (peerRole == NodeRole.CANDIDATE) {
                    // 都是候选状态 竞选 Leader
                    if (electLeader(vote)) {
                        // 对方胜选 更新自己并通知其他节点
                        updateVote(vote);
                        sendNotifications();
                    }
                    votingMap.put(vote.getNodeId(), vote);
                    // 判断是否可以结束 当前竞选环节
                    if (shouldEndVoting()) {
                        Vote nextVote;
                        while ((nextVote = recvQueue.poll(200, TimeUnit.MILLISECONDS)) != null) {
                            if (electLeader(nextVote)) {
                                recvQueue.put(nextVote);
                                return;
                            } else {
                                votingMap.put(nextVote.getNodeId(), nextVote);
                            }
                        }
                        endElection();
                    }
                } else {
                    // 加入一个已经竞选过的集群
                    int leader = vote.getLeader();
                    // 检查 Leader 节点是否可用
                    if (checkLeader(leader)) {
                        // 结束竞选
                        updateVote(vote);
                        endElection();
                    }
                }
            } else {
                if (peerRole == NodeRole.CANDIDATE) {
                    cnxManager.sendVote(vote.getNodeId(), currentVote);
                }
            }
        } catch (Exception e) {
            log.error("handleVoteRequest error from node {}", vote.getNodeId(), e);
        }

    }

    private void endElection() {
        currentRole = proposedLeader == myId ? NodeRole.LEADER : NodeRole.FOLLOWER;
        if (currentRole == NodeRole.LEADER) {
            for (Map.Entry<Integer, Vote> entry : votingMap.entrySet()) {
                if (entry.getKey() != myId) {
                    addSubNode(entry.getKey());
                }
            }
        }
        recvQueue.clear();
    }

    private boolean checkLeader(int leader) {
        Vote vote = votingMap.get(leader);
        if (vote == null) {
            return false;
        }
        return vote.getRole() == NodeRole.LEADER;
    }

    @Override
    public void sendHeartbeat() {
        if (currentRole == NodeRole.LEADER) {

        } else if (currentRole == NodeRole.FOLLOWER) {

        }
    }

    @Override
    public NodeRole getCurrentRole() {
        return currentRole;
    }

    @Override
    public void registerRoleChangeListener(RoleChangeListener listener) {
        roleChangeListeners.add(listener);
    }

    private void triggerListener() {
        for (RoleChangeListener roleChangeListener : roleChangeListeners) {
            roleChangeListener.onRoleChanged(currentRole);
        }
    }

    private void addSubNode(int nodeId) {
        InetSocketAddress address = allNodesRpcAddress.get(nodeId);
        if (address != null) {
            // TODO nodeId String 还是 Integer
            nodeMap.put(nodeId, new Node(nodeId + "", address.getHostName(), address.getPort()));
        }
    }

    /**
     * 竞选 Leader
     *
     * @param vote 竞争者
     * @return true-竞选成功
     */
    protected abstract boolean electLeader(Vote vote);

    /**
     * 是否可以结束投票环节
     *
     * @return true-结束
     */
    protected abstract boolean shouldEndVoting();

    public static class CoordinatorBuilder {
        Integer nodeId;
        Integer connectThreadsSize;
        String allNodesAddress;

        public CoordinatorBuilder nodeId(Integer nodeId) {
            this.nodeId = nodeId;
            return this;
        }

        public CoordinatorBuilder connectThreadsSize(Integer connectThreadsSize) {
            this.connectThreadsSize = connectThreadsSize;
            return this;
        }

        public CoordinatorBuilder allNodesAddress(String allNodesAddress) {
            this.allNodesAddress = allNodesAddress;
            return this;
        }

        private void validate() {
            if (nodeId == null || allNodesAddress == null || connectThreadsSize == null) {
                throw new IllegalArgumentException("nodeId and allNodesAddress and connectThreadsSize can't be null");
            }
        }

        public Coordinator buildIdCoordinator() {
            validate();
            return new IdBasedCoordinator(nodeId, allNodesAddress, connectThreadsSize);
        }

        public Coordinator buildStartupTimeCoordinator() {
            validate();
            return new StartupTimeBasedCoordinator(nodeId, allNodesAddress, connectThreadsSize);
        }
    }

    private class WorkerReceiver extends TangerThread {

        private volatile boolean running;

        public WorkerReceiver(String name) {
            super(name);
            running = true;
        }

        @Override
        public void run() {
            while (running) {
                try {
                    Vote vote = cnxManager.pollVote(3000, TimeUnit.MICROSECONDS);
                    if (vote != null) {
                        if (currentRole != NodeRole.CANDIDATE) {
                            cnxManager.sendVote(vote.getNodeId(), vote);
                            if (currentRole == NodeRole.LEADER) {
                                // 添加进子节点
                                votingMap.put(vote.getNodeId(), vote);
                                addSubNode(vote.getNodeId());
                            }
                        } else {
                            recvQueue.put(vote);
                        }
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public void end() {
            running = false;
        }
    }
}
