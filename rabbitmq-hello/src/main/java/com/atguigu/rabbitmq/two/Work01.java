package com.atguigu.rabbitmq.two;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author huajieli
 * @create 2021-07-25-20:09
 * 工作线程01（等于消费者）
 */
public class Work01 {
    //队列名称
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        DeliverCallback deliverCallback = (consumerTag,message)->{
            System.out.println("接收到的消息："+new String(message.getBody()));
        };

        CancelCallback cancelCallback = consumerTag->{
            System.out.println(consumerTag+"消费者取消消息接口回调逻辑");
        };

        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("C2等待接收消息。。。");
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
