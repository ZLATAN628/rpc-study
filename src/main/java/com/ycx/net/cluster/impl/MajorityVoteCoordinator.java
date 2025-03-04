package com.ycx.net.cluster.impl;

/**
 * 半数投票协调器
 * 收到超过半数的选票就认为可以结束当前选举
 */
public abstract class MajorityVoteCoordinator extends AbstractCoordinator {

    public MajorityVoteCoordinator(Integer nodeId, String allNodesAddress, Integer connectThreadsSize) {
        super(nodeId, allNodesAddress, connectThreadsSize);
    }

    @Override
    protected boolean shouldEndVoting() {
        // 超过半数节点投票，并且推荐的主节点已连接
        int nodesNum = allNodesElectionAddress.size();
        int half = nodesNum / 2;
        return votingMap.size() > half && votingMap.containsKey(proposedLeader);
    }
}
