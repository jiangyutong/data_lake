package com.boyoi.work.mq.callback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @author fuwp
 */
@Slf4j
@Component
public class ReturnCallbackService implements RabbitTemplate.ReturnCallback {
    /**
     * 如果消息未能投递到目标 queue 里将触发回调 returnCallback
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("returnedMessage ===> replyCode={} ,replyText={} ,exchange={} ,routingKey={}", replyCode, replyText, exchange, routingKey);
    }
}
