package com.huajieli.rabbitmq.springbootrabbitmq.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author huajieli
 * @create 2021-09-19 22:24
 *
 * 发送延迟消息
 * http://localhost:8080/ttl/sendMessage/嘻嘻嘻
 */
@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMessageController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 普通交换机X
     */
    public static final String X_EXCHANGE = "X";

    @GetMapping("/sendMessage/{message}")
    public void sendMsg(@PathVariable String message){
        log.info("当前时间{},发送一条消息给两个ttl队列:{}",new Date().toString(),message);
        rabbitTemplate.convertAndSend(X_EXCHANGE,"XA","消息来自10s的ttl:"+message);
        rabbitTemplate.convertAndSend(X_EXCHANGE,"XB","消息来自40s的ttl:"+message);
    }

}
