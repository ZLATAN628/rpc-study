package com.ycx.net.config;

/**
 * 配置管理服务（主节点实现）
 */
public interface ConfigManager {

    /** 下发集群配置 */
    void distributeConfig(ClusterConfig config);

    /** 获取当前配置 */
    ClusterConfig getCurrentConfig();
}