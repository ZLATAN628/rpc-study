package com.ycx.net.cluster;

/**
 * @author JXH
 */
public class Node {
    /** 任期 */
    private int term;
    /** 节点标识符 */
    private String nodeId;
    /** IP地址 */
    private String address;
    /** 端口 */
    private int port;

    public Node(String nodeId, String address, int port) {
        this.nodeId = nodeId;
        this.address = address;
        this.port = port;
    }
}
