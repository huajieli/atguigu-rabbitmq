package com.atguigu.rabbitmq.three;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.util.Scanner;

/**
 * @author huajieli
 * @create 2021-07-25-22:19
 *
 * 消息在手动应答中是不丢失的，放回队列重新消费
 */
public class Task03 {
    /**
     * 队列名
     */
    public static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //声明队列(页面上就可以看到了)
        //将队列声明为持久化的 durable = true;
        boolean durable = true;
        channel.queueDeclare(TASK_QUEUE_NAME,durable,false,false,null);
        //接收键盘输入信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            //设置发送的消息为持久化的（保存在磁盘中）
            channel.basicPublish("",TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());
            System.out.println("Task03消息发送成功。。。");
        }
    }
}
