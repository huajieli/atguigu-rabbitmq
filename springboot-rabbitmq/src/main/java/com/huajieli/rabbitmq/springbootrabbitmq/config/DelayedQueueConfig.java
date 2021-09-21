package com.huajieli.rabbitmq.springbootrabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huajieli
 * @create 2021-09-21 13:15
 */
@Configuration
public class DelayedQueueConfig {
    /**
     * 队列
     */
    public final static String DELAYED_QUEUE_NAME="delayed.queue";
    /**
     * 交换机
     */
    public final static String DELAYED_EXCHANGE_NAME = "delayed.name";
    /**
     * routing-key
     */
    public final static String DELAYED_ROUTING_KEY = "delayed.routingkey";
    /**
     * 交换机类型
     */
    public final static String X_DELAYED_TYPE = "direct";
    /**
     * 声明队列
     * 没有在@Bean中定义名称就是默认使用方法的名称
     */
    @Bean
    public Queue delayedQueue(){
        return new Queue(DELAYED_QUEUE_NAME);
    }
    /**
     * 声明交换机
     * 没有在@Bean中定义名称就是默认使用方法的名称
     */
    @Bean
    public CustomExchange delayedExchange(){
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("x-delayed-type",X_DELAYED_TYPE );
        /**
         * 1:交换机的名称
         * 2:交换机的类型(插件安装后mq的UI界面上多的交换机名称)
         * 3:是否需要持久化
         * 4:其他参数
         */
        return new CustomExchange(DELAYED_EXCHANGE_NAME,"x-delayed-message",true,false,paramsMap);
    }
    /**
     * 绑定延迟队列和延迟交换机
     */
    @Bean
    public Binding delayedQueueBindDelayedExchange(@Qualifier("delayedQueue") Queue delayedQueue,
                                                   @Qualifier("delayedExchange") CustomExchange delayedExchange){
        return BindingBuilder.bind(delayedQueue).to(delayedExchange).with(DELAYED_ROUTING_KEY).noargs();
    }


}
