package com.huajieli.rabbitmq.springbootrabbitmq.controller;

import com.huajieli.rabbitmq.springbootrabbitmq.config.ConfirnConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huajieli
 * @create 2021-09-21 17:10
 * 确认消息
 */
@Slf4j
@RestController
@RequestMapping("/confirm")
public class ProductController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMessage/{message}")
    public void sendMessage(@PathVariable String message){
        CorrelationData correlationData = new CorrelationData("1");
        //rabbitTemplate.convertAndSend(ConfirnConfig.CONFIRM_EXCHANGE,ConfirnConfig.CONFIRM_ROUTING_KEY,message,correlationData);
        //模仿消息发送失败交换机原因（故意将交换机改成不存在的名字）
        //rabbitTemplate.convertAndSend(ConfirnConfig.CONFIRM_EXCHANGE+"不存在的交换机",ConfirnConfig.CONFIRM_ROUTING_KEY,message,correlationData);
        /**
         * 模仿消息发送失败队列原因（故意将routingKey改成其他）
         * 下面虽然也回调了（交换机回调），但是问题出在没发送到队列，所以消息其实是丢失了
         */
        rabbitTemplate.convertAndSend(ConfirnConfig.CONFIRM_EXCHANGE,ConfirnConfig.CONFIRM_ROUTING_KEY+"不是对应声明队列的key",message,correlationData);

        log.info("发送的消息：{}",message);
    }
}
