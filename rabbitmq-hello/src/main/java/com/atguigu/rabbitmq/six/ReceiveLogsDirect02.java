package com.atguigu.rabbitmq.six;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

/**
 * @author huajieli
 * @create 2021-09-12 21:38
 *
 * direct交换机类型的消费者02
 */
public class ReceiveLogsDirect02 {
    /**
     * 交换机名称
     */
    public static final String EXCHANGE_NAME = "direct_logs";

    /**
     * 队列名称
     */
    public static final String QUEUE_NAME = "disk";

    /**
     * routingKey
     */
    public static final String ROUTING_KEY = "error";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //绑定队列和交换机
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,ROUTING_KEY);

        //消费消息
        //接受消息时的回调
        DeliverCallback deliverCallback = (consumerTag, message)->{
            System.out.println("ReceiveLogsDirect02控制台打印接收到的消息："+new String(message.getBody(), StandardCharsets.UTF_8));
        };
        //取消消息时的回调
        CancelCallback cancelCallback = consumerTag->{
            System.out.println("消息消费被中断");
        };
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);


    }

}
