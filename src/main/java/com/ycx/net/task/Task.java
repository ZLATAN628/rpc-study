package com.ycx.net.task;

/**
 * @author JXH
 */
public class Task {
    // 唯一任务标识
    private String taskId;
    // 任务类型分类
    private String taskType;
    // 执行优先级
    private int priority;
    // 任务输入数据，序列化
    private byte[] payload;
}
