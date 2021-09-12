package com.atguigu.rabbitmq.five;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

/**
 * @author huajieli
 * @create 2021-09-12 20:23
 * <p>
 * 消息的接收
 */
public class ReceiveLogs01 {

    /**
     * 交换机名称
     */
    public static final String EXCHANGE_NAME = "logs";
    /**
     * 交换机类型
     */
    public static final String EXCHANGE_TYPE="fanout";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME,EXCHANGE_TYPE);
        /**
         * 声明一个临时队列：队列名称是随机的
         * 当消费者断开和队列的连接后队列会自定删除（不像这节课之前的例子，队列一直还在，其实已经没有用处）
         */
        String queuename = channel.queueDeclare().getQueue();
        /**
         * 绑定交换机和队列
         * 参数1：队列名
         * 参数2：交换机名
         * 参数3：routingKey
         */
        channel.queueBind(queuename,EXCHANGE_NAME,"");
        System.out.println("01等待接受消息，把接收消息打印在屏幕上......");

        //接受消息时的回调
        DeliverCallback deliverCallback = (consumerTag,message)->{
            System.out.println("01控制台打印接收到的消息："+new String(message.getBody(), StandardCharsets.UTF_8));
        };
        //取消消息时的回调
        CancelCallback cancelCallback = consumerTag->{
            System.out.println("消息消费被中断");
        };
        channel.basicConsume(queuename,true,deliverCallback,cancelCallback);
    }
}
