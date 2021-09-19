package com.atguigu.rabbitmq.eigth;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

/**
 * @author huajieli
 * @create 2021-09-19 15:24
 * 死信队列
 * 死信队列的消费者02
 */
public class Consumer02 {
    /**
     * 死信队列
     */
    private static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        System.out.println("Consumer02等待接受消息，把接收消息打印在屏幕上......");

        //接受消息时的回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("控制台Consumer02打印接收到的消息：" + new String(message.getBody(), StandardCharsets.UTF_8));
        };
        //取消消息时的回调
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("消息消费被中断");
        };
        channel.basicConsume(DEAD_QUEUE, true, deliverCallback, cancelCallback);

    }
}
