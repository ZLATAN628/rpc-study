package com.ycx.net.monitor;

import java.util.List;
import java.util.Map;

/**
 * 综合监控数据载体（按节点维度上报）
 */
public class MonitoringNode {
    // ====== 基础标识 ======
    private String nodeId;              // 节点唯一标识
    private long timestamp;            // 数据采集时间戳(ms)
    private DataSourceType sourceType; // 数据来源类型(枚举)
    private String serviceName;        // 关联服务名称

    // ====== 资源指标 ======
    private CpuStats cpu;              // CPU使用详情
    private MemoryStats memory;        // 内存使用详情
    private List<DiskStats> disks;     // 磁盘分区详情
    private NetworkStats network;      // 网络流量统计

    // ====== 任务执行指标 ======
    private int activeTasks;           // 正在执行的任务数
    private int queuedTasks;           // 等待队列中的任务数
    private double taskSuccessRate;    // 任务成功率(0-1)
    private long avgTaskDuration;      // 平均任务耗时(ms)

    // ====== JVM指标 ======
    private JvmStats jvm;              // JVM运行状态

    // ====== 服务状态 ======
    private HealthStatus healthStatus; // 节点健康度评估
    private long lastHeartbeat;        // 最近心跳时间

    // ====== 扩展指标 ======
    private Map<String, Double> customMetrics; // 自定义监控指标

    // ====== 元数据 ======
    private int dataVersion = 1;       // 数据结构版本
    private String checksum;           // 数据校验码

    // ====== 历史命令列表 ======
    private List<CommandTrace> commandTraces;

    // 枚举定义
    public enum DataSourceType {
        SYSTEM, SERVICE, TASK, CLUSTER
    }

    public enum HealthStatus {
        GREEN, YELLOW, RED
    }

    // ====== 嵌套指标类 ======

    /** CPU指标 */
    public static class CpuStats {
        private double userUsage;    // 用户态使用率(%)
        private double systemUsage;  // 内核态使用率(%)
        private double loadAverage;  // 平均负载(1min)
        private int cores;          // 物理核心数
    }

    /** 内存指标 */
    public static class MemoryStats {
        private long total;        // 总内存(bytes)
        private long available;     // 可用内存(bytes)
        private long swapUsed;      // Swap使用量(bytes)
    }

    /** 磁盘指标 */
    public static class DiskStats {
        private String mountPoint;  // 挂载点
        private long totalSpace;    // 总容量(bytes)
        private long usedSpace;     // 已用空间(bytes)
        private double ioWait;      // IO等待时间(%)
    }

    /** 网络指标 */
    public static class NetworkStats {
        private long bytesSent;     // 累计发送字节
        private long bytesRecv;     // 累计接收字节
        private int tcpConnections; // 当前TCP连接数
    }

    /** JVM指标 */
    public static class JvmStats {
        private long heapUsed;      // 已用堆内存(bytes)
        private long heapMax;       // 最大堆内存(bytes)
        private int threads;       // 活动线程数
        private long gcTime;        // GC累计耗时(ms)
        private int gcCount;        // GC总次数
    }

    // getters/setters...
}

