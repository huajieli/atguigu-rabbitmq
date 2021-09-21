package com.huajieli.rabbitmq.springbootrabbitmq.consumer;

import com.huajieli.rabbitmq.springbootrabbitmq.config.ConfirnConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author huajieli
 * @create 2021-09-21 21:40
 *
 * 报警消费者
 */
@Slf4j
@Component
public class WaringConsumer {
    /**
     * 监听接收报警消息
     */
    @RabbitListener(queues = ConfirnConfig.WARNING_QUEUE)
    public void reveiveWaringMsg(Message message){
        String msg = new String(message.getBody());
        log.error("报警发现不可路由的消息:{}",msg);
    }
}
