package com.huajieli.rabbitmq.springbootrabbitmq.consumer;

import com.huajieli.rabbitmq.springbootrabbitmq.config.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author huajieli
 * @create 2021-09-21 15:37
 * 消费者，延迟队列基于插件的
 */
@Slf4j
@Component
public class DelayedQueueConsumer {
    @RabbitListener(queues = DelayedQueueConfig.DELAYED_QUEUE_NAME)
    public void receiveDelayQueue(Message message){
        String msg=new String(message.getBody());
        log.info("当前时间{},延迟队列基于插件的消费者接收到了{}",new Date().toString(),msg);
    }
}
