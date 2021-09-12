package com.atguigu.rabbitmq.five;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author huajieli
 * @create 2021-09-12 21:15
 * <p>
 * 消息发送
 */
public class EmitLog {
    /**
     * 交换机名称
     */
    public static final String EXCHANGE_NAME = "logs";
    /**
     * 交换机类型
     */
    public static final String EXCHANGE_TYPE = "fanout";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //这里声明的交换机已经在消费者那里声明了（视频里做了注释要不然会报错）
        //channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);

        Scanner scanner = new Scanner(System.in);
        while ((scanner.hasNext())) {
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME,"",null,message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发出了消息："+message);
        }

    }
}
