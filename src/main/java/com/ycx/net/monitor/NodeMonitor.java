package com.ycx.net.monitor;

/**
 * 节点监控服务（所有节点实现）
 */
public interface NodeMonitor {

    /** 采集节点健康状态 */
    NodeHealth getNodeHealth();

    /** 收集性能指标 */
    PerformanceMetrics collectMetrics();

    /** 上报监控数据 */
    void reportMetrics(MonitoringNode data);
}