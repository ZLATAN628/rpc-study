package com.ycx.net.monitor;

import java.time.Duration;
import java.util.List;

/**
 * 监控管理服务（主节点实现）
 */
public interface MonitoringManager {

    /** 获取集群整体状态 */
    ClusterHealth getClusterHealth();

    /** 查询节点状态历史 */
    List<MonitoringNode> queryHistoricalData(String nodeId, Duration period);

    /** 任务指标 */
    List<MonitoringTask> queryTasksProcess(String nodeId, Duration period);

}
