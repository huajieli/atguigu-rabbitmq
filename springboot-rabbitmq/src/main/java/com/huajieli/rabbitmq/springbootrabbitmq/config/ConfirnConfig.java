package com.huajieli.rabbitmq.springbootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author huajieli
 * @create 2021-09-21 16:35
 * <p>
 * 发布确认配置类
 */
@Configuration
public class ConfirnConfig {
    /**
     * 交换机
     */
    public final static String CONFIRM_EXCHANGE = "confirm_exchange";
    /**
     * 队列
     */
    public final static String CONFIRM_QUEUE = "confirm_queue";
    /**
     * routingKey
     */
    public final static String CONFIRM_ROUTING_KEY = "routing_key";
    /**
     * 备份交换机
     */
    public final static String BACKUP_EXCHANGE = "backup_exchange";
    /**
     * 备份队列
     */
    public final static String BACKUP_QUEUE = "backup_queue";
    /**
     * 报警队列
     */
    public final static String WARNING_QUEUE = "warning_queue";

    /**
     * 声明确认交换机
     *
     * @return
     */
    @Bean
    public DirectExchange confirmExchange() {
        //return new DirectExchange(CONFIRM_EXCHANGE);增加了备份交换机就不能这样写了
        //声明普通交换机的备份交换机
        ExchangeBuilder exchangeBuilder = ExchangeBuilder.directExchange(CONFIRM_EXCHANGE).durable(true)
                .withArgument("alternate-exchange", BACKUP_EXCHANGE);
        return exchangeBuilder.build();
    }

    /**
     * 声明备份交换机
     *
     * @return
     */
    @Bean("backupExchange")
    public FanoutExchange backupExchange() {
        return new FanoutExchange(CONFIRM_EXCHANGE);
    }

    /**
     * 声明队列
     *
     * @return
     */
    @Bean
    public Queue confirmQueue() {
        //return new Queue(CONFIRM_QUEUE);
        return QueueBuilder.durable(CONFIRM_QUEUE).build();
    }

    /**
     * 声明备份队列
     */
    @Bean("backupQueue")
    public Queue backupQueue() {
        return QueueBuilder.durable(BACKUP_QUEUE).build();
    }

    /**
     * 声明报警队列
     */
    @Bean("warningQueue")
    public Queue warningQueue() {
        return QueueBuilder.durable(WARNING_QUEUE).build();
    }

    /**
     * 绑定确认队列到确认交换机
     */
    @Bean
    public Binding confirmQueueBindConfirmExchange(@Qualifier("confirmExchange") DirectExchange confirmExchange, @Qualifier("confirmQueue") Queue confirmQueue) {
        return BindingBuilder.bind(confirmQueue).to(confirmExchange).with(CONFIRM_ROUTING_KEY);
    }

    /**
     * 绑定备份队列到备份交换机
     */
    @Bean
    public Binding backupQueueBindBackupExchange(@Qualifier("backupQueue") Queue backupQueue,
                                                 @Qualifier("backupExchange") FanoutExchange backupExchange) {
        return BindingBuilder.bind(backupQueue).to(backupExchange);
    }

    /**
     * 绑定报警队列到备份交换机
     */
    @Bean
    public Binding waringQueueBindBackupExchange(@Qualifier("warningQueue") Queue warningQueue,
                                                 @Qualifier("backupExchange") FanoutExchange backupExchange) {
        return BindingBuilder.bind(warningQueue).to(backupExchange);
    }


}
