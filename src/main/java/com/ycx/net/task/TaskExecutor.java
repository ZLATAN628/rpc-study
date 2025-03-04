package com.ycx.net.task;

public interface TaskExecutor {

    /** 执行具体任务 */
    TaskResult executeTask(Task task);

    /** 上报任务状态 */
    void reportTaskStatus(String taskId, TaskStatus status);

}
