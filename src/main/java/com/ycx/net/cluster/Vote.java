package com.ycx.net.cluster;

import java.nio.ByteBuffer;

public class Vote {
    /*
     * Proposed leader
     */
    int leader;

    /*
     * current role of sender
     */
    NodeRole role;

    /*
     * Address of sender
     */
    int nodeId;

    long startupTime;

    public Vote(int leader, NodeRole role, int nodeId) {
        this.leader = leader;
        this.role = role;
        this.nodeId = nodeId;
        this.startupTime = System.currentTimeMillis();
    }

    public Vote(int nodeId, ByteBuffer buffer) {
        buffer.clear();
        if (buffer.capacity() < 20) {
            throw new RuntimeException("vote buffer too short");
        }
        // TODO
        this.role = convertToRole(buffer.getInt());
        this.leader = buffer.getInt();
        this.nodeId = buffer.getInt();
        this.startupTime = buffer.getLong();
    }

    private NodeRole convertToRole(int i) {
        switch (i) {
            case 0:
                return NodeRole.LEADER;
            case 1:
                return NodeRole.FOLLOWER;
            case 2:
                return NodeRole.CANDIDATE;
            default:
                throw new RuntimeException("unknown role");
        }
    }

    public ByteBuffer toByteBuffer() {
        byte[] bytes = new byte[20];
        ByteBuffer buffer = ByteBuffer.wrap(bytes);

        buffer.clear();
        buffer.putInt(role.ordinal());
        buffer.putInt(leader);
        buffer.putInt(nodeId);
        buffer.putLong(startupTime);

        return buffer;
    }

    public int getLeader() {
        return leader;
    }

    public void setLeader(int leader) {
        this.leader = leader;
    }

    public NodeRole getRole() {
        return role;
    }

    public void setRole(NodeRole role) {
        this.role = role;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public long getStartupTime() {
        return startupTime;
    }

    public void setStartupTime(long startupTime) {
        this.startupTime = startupTime;
    }
}
