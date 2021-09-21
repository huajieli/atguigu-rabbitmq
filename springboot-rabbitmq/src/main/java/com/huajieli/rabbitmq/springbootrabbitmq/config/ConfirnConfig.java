package com.huajieli.rabbitmq.springbootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author huajieli
 * @create 2021-09-21 16:35
 *
 * 发布确认配置类
 */
@Configuration
public class ConfirnConfig {
    /**
     * 交换机
     */
    public final static String CONFIRM_EXCHANGE="confirm_exchange";
    /**
     * 队列
     */
    public final static String CONFIRM_QUEUE="confirm_queue";
    /**
     * routingKey
     */
    public final static String CONFIRM_ROUTING_KEY="routing_key";

    /**
     * 声明交换机
     * @return
     */
    @Bean
    public DirectExchange confirmExchange(){
        return new DirectExchange(CONFIRM_EXCHANGE);
    }

    /**
     * 声明队列
     * @return
     */
    @Bean
    public Queue confirmQueue(){
        //return new Queue(CONFIRM_QUEUE);
        return QueueBuilder.durable(CONFIRM_QUEUE).build();
    }
    /**
     * 绑定
     */
    @Bean
    public Binding confirmQueueBindConfirmExchange(@Qualifier("confirmExchange") DirectExchange confirmExchange, @Qualifier("confirmQueue") Queue confirmQueue){
        return BindingBuilder.bind(confirmQueue).to(confirmExchange).with(CONFIRM_ROUTING_KEY);
    }



}
