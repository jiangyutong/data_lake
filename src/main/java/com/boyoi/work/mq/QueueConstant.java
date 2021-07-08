package com.boyoi.work.mq;

/**
 * @author fuwp
 */
public interface QueueConstant {
    /**
     * 写入数据队列
     */
    String WRITE_QUEUE = "DATA_LAKE_WRITE_QUEUE";
    /**
     * 查询数据队列
     */
    String QUERY_QUEUE = "DATA_LAKE_QUERY_QUEUE";
    /**
     * 写交换机
     */
    String WRITE_EXCHANGE = "DATA_LAKE_WRITE_EXCHANGE";
    /**
     * 写交换机
     */
    String QUERY_EXCHANGE = "DATA_LAKE_QUERY_EXCHANGE";
}
