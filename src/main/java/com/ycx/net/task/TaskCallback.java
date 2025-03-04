package com.ycx.net.task;

/**
 * 任务结果回调接口
 * （由主节点实现并注册到TaskDispatcher）
 * @author JXH
 */
public interface TaskCallback {

    /**
     * 任务成功完成回调
     * @param taskId 任务唯一标识
     * @param result 任务执行结果（包含输出数据/状态码等）
     */
    void onSuccess(String taskId, TaskResult result);

    /**
     * 任务执行失败回调
     * @param taskId 任务唯一标识
     * @param cause 失败原因（包含异常堆栈/错误码等）
     */
    void onFailure(String taskId, Throwable cause);

    /**
     * 可选：任务进度通知（适用于长任务）
     * @param taskId 任务唯一标识
     * @param progress 进度百分比(0-100)
     * @param checkpoint 进度检查点描述
     */
    default void onProgress(String taskId, int progress, String checkpoint) {
        // 默认空实现
    }

}
