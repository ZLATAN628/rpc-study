package com.ycx.net.task;

/**
 * @author JXH
 */
public class TaskResult {
    // 执行耗时(ms)
    private long executionTime;
    // 自定义状态码
    private int statusCode;
    // 任务输出数据，包括异常,序列化
    private byte[] output;
}
