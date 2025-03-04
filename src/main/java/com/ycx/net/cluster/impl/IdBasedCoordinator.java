package com.ycx.net.cluster.impl;

import com.ycx.net.cluster.Vote;

public class IdBasedCoordinator extends MajorityVoteCoordinator {

    public IdBasedCoordinator(Integer nodeId, String allNodesAddress, Integer connectThreadsSize) {
        super(nodeId, allNodesAddress, connectThreadsSize);
    }

    @Override
    protected boolean electLeader(Vote vote) {
        return this.proposedLeader < vote.getLeader();
    }

}
