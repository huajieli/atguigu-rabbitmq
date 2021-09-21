package com.huajieli.rabbitmq.springbootrabbitmq.consumer;

import com.huajieli.rabbitmq.springbootrabbitmq.config.ConfirnConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author huajieli
 * @create 2021-09-21 17:17
 */
@Slf4j
@Component
public class Consumer {
    @RabbitListener(queues = ConfirnConfig.CONFIRM_QUEUE)
    public void reveiveConfirmMsg(Message message){
        String msg = new String(message.getBody());
        log.info("队列{}接收的消息是:{}",ConfirnConfig.CONFIRM_QUEUE,msg);
    }
}
