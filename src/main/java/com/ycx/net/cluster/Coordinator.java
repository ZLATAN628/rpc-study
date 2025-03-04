package com.ycx.net.cluster;

/**
 * 选举协调器
 * @author JXH
 */
public interface Coordinator {

    /** 发起选举 */
    void startElection();

    /** 处理投票请求 */
    void handleVoteRequest(Vote vote);

    /** 心跳检测机制 */
    void sendHeartbeat();

    /** 获取当前角色 */
    NodeRole getCurrentRole();

    /** 注册角色变更监听器 */
    void registerRoleChangeListener(RoleChangeListener listener);

}
