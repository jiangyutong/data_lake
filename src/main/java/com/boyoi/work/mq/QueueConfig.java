package com.boyoi.work.mq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author fuwp
 */
@Configuration
public class QueueConfig {

    @Bean
    public Queue writeQueue() {
        return new Queue(QueueConstant.WRITE_QUEUE, true, false, false);
    }

    @Bean
    public Queue queryQueue() {
        return new Queue(QueueConstant.QUERY_QUEUE, true, false, false);
    }

    @Bean
    public FanoutExchange writeExchange() {
        return new FanoutExchange(QueueConstant.WRITE_EXCHANGE);
    }

    @Bean
    public FanoutExchange queryExchange() {
        return new FanoutExchange(QueueConstant.QUERY_EXCHANGE);
    }

    @Bean
    public Binding writeFanoutExchangeAndQueue(FanoutExchange writeExchange, Queue writeQueue) {
        return BindingBuilder.bind(writeQueue).to(writeExchange);
    }

    @Bean
    public Binding queryFanoutExchangeAndQueue(FanoutExchange queryExchange, Queue queryQueue) {
        return BindingBuilder.bind(queryQueue).to(queryExchange);
    }
}
