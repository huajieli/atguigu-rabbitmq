package com.atguigu.rabbitmq.six;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author huajieli
 * @create 2021-09-12 21:52
 * <p>
 * direct类型交换机的生产者
 */
public class DirectLogs {
    /**
     * 交换机名称
     */
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        /**
         * 默认为error,则输入的生成的消息发送给routingKey为error的ReceiveLogsDirect02这个消费者
         */
        String routingKey = "error";
        Scanner scanner = new Scanner(System.in);
        while ((scanner.hasNext())) {
            String message = scanner.next();
            if(message.contains("1")){
                routingKey="info";
            }else if(message.contains("2")){
                routingKey="warning";
            }else{
                routingKey="error";
            }
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发出了消息：" + message);
        }

    }
}
