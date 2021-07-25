package com.atguigu.rabbitmq.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author huajieli
 * @create 2021-07-25-15:27
 * 生产者测试类
 */
public class Produce {
    //队列名称
    public static final String QUEUE_NAME = "hello";

    //发送消息
    public static void main(String[] args) throws IOException, TimeoutException {
        //建立一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //工厂IP:连接RabbitMQ的队列
        factory.setHost("192.168.56.10");
        factory.setUsername("admin");
        factory.setPassword("123");

        //新建连接
        Connection connection = factory.newConnection();
        //创建信道
        Channel channel = connection.createChannel();
        //省去Exchange
        /**
         * 创建一个队列
         * 参数1：队列的名字
         * 参数2：是否持久化（磁盘）默认情况消息存在内存中
         * 参数3：该队列是否只供一个消费者进行消费，是否进行消息共享，true可以多个消费者，false只能一个消费者消费
         * 参数4：是否自动删除(最后一个消费者断开连接以后)
         * 参数5：其他参数
         */
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //发消息
        String message = "hello world!";
        /**
         * 参数1：发送到哪个交换机(本次忽略这个)
         * 参数2：路由的key值是哪一个（本次就是队列的名字）
         * 参数3：其他参数信息（本次null）
         * 参数4：发送的消息
         *
         *
         */
        channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
        System.out.println("发送消息完毕！");
    }
}
