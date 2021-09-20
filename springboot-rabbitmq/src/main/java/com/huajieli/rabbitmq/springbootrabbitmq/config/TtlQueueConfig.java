package com.huajieli.rabbitmq.springbootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huajieli
 * @create 2021-09-19 20:41
 * 配置类代码
 */
@Configuration
public class TtlQueueConfig {
    /**
     * 普通交换机X
     */
    public static final String X_EXCHANGE = "X";
    /**
     * 死信交换机Y
     */
    public static final String Y_DEAD_EXCHANGE = "Y";
    /**
     * 普通队列QA
     */
    public static final String QA_QUEUE = "QA";
    /**
     * 普通队列QB
     */
    public static final String QB_QUEUE = "QB";
    /**
     * 死信队列QD
     */
    public static final String QD_DEAD_QUEUE = "QD";

    /**
     * 对于过期时间扩展性差的问题
     * 普通队列QC
     */
    public static final String QC_QUEUE = "QC";

    /**
     * 声明X_EXCHANGE
     */
    @Bean("xExchange")
    public DirectExchange xExchange() {
        return new DirectExchange(X_EXCHANGE);
    }

    /**
     * 声明Y_DEAD_EXCHANGE
     */
    @Bean
    public DirectExchange yExchange() {
        return new DirectExchange(Y_DEAD_EXCHANGE);
    }

    /**
     * 声明普通队列QA
     * ttl 10s
     */
    @Bean("queueA")
    public Queue queueA() {
        Map<String, Object> paramsMap = new HashMap(3);
        //设置队列的死信交换机
        paramsMap.put("x-dead-letter-exchange", Y_DEAD_EXCHANGE);
        //设置死信队列RoutingKey
        paramsMap.put("x-dead-letter-routing-key", "YD");
        //设置TTL(单位ms)
        paramsMap.put("x-message-ttl", 10000);
        return QueueBuilder.durable(QA_QUEUE).withArguments(paramsMap).build();
    }

    /**
     * 声明普通队列QB
     * ttl 40s
     */
    @Bean("queueB")
    public Queue queueB() {
        Map<String, Object> paramsMap = new HashMap();
        //设置队列的死信交换机
        paramsMap.put("x-dead-letter-exchange", Y_DEAD_EXCHANGE);
        //设置死信队列RoutingKey
        paramsMap.put("x-dead-letter-routing-key", "YD");
        //设置TTL(单位ms)
        paramsMap.put("x-message-ttl", 40000);
        return QueueBuilder.durable(QB_QUEUE).withArguments(paramsMap).build();
    }

    /**
     * 声明普通队列QB
     * 不设置ttl时间,解决扩展性差的问题
     */
    @Bean("queueC")
    public Queue queueC() {
        Map<String, Object> paramsMap = new HashMap(3);
        //设置队列的死信交换机
        paramsMap.put("x-dead-letter-exchange", Y_DEAD_EXCHANGE);
        //设置死信队列RoutingKey
        paramsMap.put("x-dead-letter-routing-key", "YD");
        return QueueBuilder.durable(QC_QUEUE).withArguments(paramsMap).build();
    }

    /**
     * 死信队列QD
     */
    @Bean("queueD")
    public Queue queueD() {
        return QueueBuilder.durable(QD_DEAD_QUEUE).build();
    }

    /**
     * 绑定QA->EX
     * 使用注解注入要绑定的队列和交换机
     */
    @Bean
    public Binding queueABindingX(@Qualifier("queueA") Queue queueA, @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueA).to(xExchange).with("XA");
    }

    /**
     * 绑定QB->EX
     * 使用注解注入要绑定的队列和交换机
     */
    @Bean
    public Binding queueBBindingX(@Qualifier("queueB") Queue queueB, @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueB).to(xExchange).with("XB");
    }
    /**
     * 绑定QC->EX
     * 使用注解注入要绑定的队列和交换机
     */
    @Bean
    public Binding queueCBindingX(@Qualifier("queueC") Queue queueC, @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueC).to(xExchange).with("XC");
    }
    /**
     * 绑定QD->EY
     * 使用注解注入要绑定的队列和交换机
     */
    @Bean
    public Binding queueDBindingX(@Qualifier("queueD") Queue queueD, @Qualifier("yExchange") DirectExchange yExchange) {
        return BindingBuilder.bind(queueD).to(yExchange).with("YD");
    }

}



























