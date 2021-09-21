package com.huajieli.rabbitmq.springbootrabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author huajieli
 * @create 2021-09-21 17:31
 *
 * 生产着发送消息的回调函数
 * 实现RabbitTemplate.ConfirmCallback该内部类
 */
@Slf4j
@Component
public class MyCallBack implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnsCallback {

    /**
     * 注入RabbitTemplate
     */
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 将自定义的MyCallBack的（下面用this代替）注入rabbitTemplate
     * 这样才会触发回调
     * 注解@PostConstruct的执行顺序
     * Constructor(构造方法) -> @Autowired(依赖注入) -> @PostConstruct(注释的方法)
     */
    @PostConstruct
    public void init(){
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }



    /**
     * 交换机确认回调方法
     * 1：交换机成功接收回调
     *  1.1：correlationData 保存回调消息的ID以及相关信息
     *  1.2：ack=true表示交换机成功接受到消息
     *  1.3：case 此时为null
     * 2：交换机失败接收回调
     *  2.1：correlationData 保存回调消息的ID以及相关信息
     *  2.2：ack=false表示交换机失败接收消息
     *  2.3：case 此时内容为失败的原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData.getId()==null?"":correlationData.getId();
        if(ack){
            log.info("交换机接收到了id为{}的消息",id);
        }else{
            log.info("交换机还未接收到id为{}的消息,由于原因{}",id,cause);
        }
    }


    /**
     * 当消息无法路由的时候的回调方法(即发送不到对应的队列)
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("1111111111");
        log.error("消息{}，因为队列的关系被交换机{}退回，退回原因是{}，路由key:{}",new String(message.getBody()),exchange,replyText,routingKey);
    }

    /**
     * 当消息无法路由的时候的回调方法(即发送不到对应的队列)
     */
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.error("消息{}，因为队列的关系被交换机{}退回，退回原因是{}，路由key:{}",returnedMessage.getMessage(),returnedMessage.getExchange(),returnedMessage.getReplyText(),returnedMessage.getRoutingKey());
    }

















}
