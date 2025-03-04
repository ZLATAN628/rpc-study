package com.ycx.net.cluster.impl;

import com.ycx.net.cluster.Vote;

public class StartupTimeBasedCoordinator extends MajorityVoteCoordinator {

    public StartupTimeBasedCoordinator(Integer nodeId, String allNodesAddress, Integer connectThreadsSize) {
        super(nodeId, allNodesAddress, connectThreadsSize);
    }

    @Override
    protected boolean electLeader(Vote vote) {
        return vote.getStartupTime() > proposedLeaderStartupTime;
    }

}
