package com.ycx.net.task;

import java.util.Map;

/**
 * 任务控制指令（主节点接收的外部指令载体）
 */
public class TaskCommand {
    // 指令元数据
    private String commandId;          // 唯一指令标识(UUID)
    private CommandType commandType;   // 指令类型枚举
    private long timestamp;            // 指令创建时间戳
    private String initiator;          // 指令发起方标识

    // 指令内容
    private String targetTaskId;       // 关联的任务ID（用于UPDATE/CANCEL等操作）
    private TaskSpecification taskSpec;// 任务详情（用于CREATE操作）
    private Map<String, String> params;// 扩展参数

    // 校验信息
    private String checksum;           // 数据校验码
    private int version = 1;           // 协议版本

    // 枚举定义
    public enum CommandType {
        CREATE_TASK,    // 创建新任务
        CANCEL_TASK,    // 取消进行中任务
        UPDATE_CONFIG,  // 更新任务配置
        QUERY_STATUS,   // 查询任务状态
        EMERGENCY_STOP  // 紧急停止指令
    }

    // 嵌套任务描述类
    public static class TaskSpecification {
        private String taskType;       // 任务类型标识
        private Priority priority;    // 执行优先级
        private byte[] payload;        // 任务负载数据
        private String[] targetNodes;  // 指定执行节点(null表示自动分配)
        private int maxRetries;        // 最大重试次数
        private long timeout;          // 超时时间(ms)

        public enum Priority { LOW, NORMAL, HIGH }
    }


}