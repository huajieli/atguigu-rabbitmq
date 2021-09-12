package com.atguigu.rabbitmq.four;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.util.UUID;

/**
 * @author huajieli
 * @create 2021-09-12 16:11
 */
public class ConfirmMessage {
    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        //1:单个确认
        //ConfirmMessage.publishMessageIndividually();
        //2：批量确认
        ConfirmMessage.publishMessageBatch();
        //3：异步确认

    }

    /**
     * 单个确认
     */
    public static void publishMessageIndividually() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //队列的声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();
        //批量发送消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            //单个消息马上进行确认
            boolean flag = channel.waitForConfirms();
            if(flag){
                System.out.println("消息发送成功"+i);
            }
        }
        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("单独确认发送"+MESSAGE_COUNT+"条耗时"+(end-begin)+"ms");
    }

    /**
     * 批量确认
     */
    public static void publishMessageBatch() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //队列的声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();
        //每次批量确认的size
        int confirmSize = 100;
        //批量发送消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            if(i%confirmSize==0){
                boolean flag = channel.waitForConfirms();
                if(flag){
                    System.out.println("第"+i+"条前的数据发送成功");
                }
            }
        }
        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("批量确认发送"+MESSAGE_COUNT+"条耗时"+(end-begin)+"ms");
    }
}
