package com.ycx.net.config;

/**
 * 配置同步服务（所有节点实现）
 */
public interface ConfigSyncService {

    /** 应用新配置 */
    void applyNewConfig(ClusterConfig config);

    /** 获取节点本地配置 */
    ClusterConfig getLocalConfig();
}