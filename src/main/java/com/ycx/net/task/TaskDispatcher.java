package com.ycx.net.task;


import com.ycx.net.cluster.Node;

/**
 * @author JXH
 */
public interface TaskDispatcher {

    /** 接收外部指令 */
    void receiveCommand(TaskCommand command);

    /** 分配任务到指定节点 */
    void assignTask(Task task, Node target);

    /** 注册任务结果回调 */
    void registerTaskCallback(TaskCallback callback);

    /** 支持命令处理流水线增强 */
    void processCommand(TaskCommand command);
}
