package com.atguigu.rabbitmq.two;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

/**
 * @author huajieli
 * @create 2021-07-25-20:39
 * 生产者
 */
public class Task01 {
    //队列的名字
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        /**
         * 创建一个队列
         * 参数1：队列的名字
         * 参数2：是否持久化（磁盘）默认情况消息存在内存中
         * 参数3：该队列是否只供一个消费者进行消费，是否进行消息共享，true可以多个消费者，false只能一个消费者消费
         * 参数4：是否自动删除(最后一个消费者断开连接以后)
         * 参数5：其他参数
         */
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //从控制台接收信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();

            /**
             * 参数1：发送到哪个交换机(本次忽略这个)
             * 参数2：路由的key值是哪一个（本次就是队列的名字）
             * 参数3：其他参数信息（本次null）
             * 参数4：发送的消息
             */
        channel.basicPublish("",QUEUE_NAME,null, message.getBytes());
        System.out.println("消息发送完成");
        }
    }
}
