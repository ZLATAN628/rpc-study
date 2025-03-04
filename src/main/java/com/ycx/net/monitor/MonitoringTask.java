package com.ycx.net.monitor;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务处理状态追踪（按任务维度）
 */
public class MonitoringTask {
    // ====== 任务标识 ======
    private String taskId;           // 任务唯一ID
    private String taskType;         // 任务类型标识
    private String ownerNode;        // 执行节点ID

    // ====== 进度追踪 ======
    private int progress;           // 完成进度(0-100)
    private long itemsProcessed;    // 已处理项数
    private long totalItems;       // 总需处理项数
    private String currentStage;    // 当前处理阶段描述

    // ====== 状态机 ======
    private ProcessState state;     // 当前处理状态
    private long stateTimestamp;   // 状态变更时间

    // ====== 错误追踪 ======
    private List<TaskError> errors = new ArrayList<>(); // 错误历史

    // ====== 资源消耗 ======
    private ResourceUsage resources; // 资源消耗详情

    // ====== 时间轴 ======
    private long startTime;         // 任务开始时间戳
    private long lastUpdateTime;    // 最后更新时间戳

    // ====== 嵌套类 ======
    /** 处理状态枚举 */
    public enum ProcessState {
        PENDING, RUNNING, PAUSED, COMPLETED, FAILED, RETRYING
    }

    /** 错误详情 */
    public static class TaskError {
        private long timestamp;     // 发生时间
        private String errorCode;   // 错误分类码
        private String message;     // 错误描述
        private String stackTrace; // 异常堆栈(可选)
    }

    /** 资源消耗统计 */
    public static class ResourceUsage {
        private double cpuPercent;  // CPU占用率(%)
        private long memoryUsed;    // 内存使用量(bytes)
        private long diskBytesRead; // 磁盘读取总量
    }

    // getters/setters...
}
