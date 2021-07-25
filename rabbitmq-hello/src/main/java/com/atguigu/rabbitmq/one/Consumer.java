package com.atguigu.rabbitmq.one;

import com.rabbitmq.client.*;

/**
 * @author huajieli
 * @create 2021-07-25-17:13
 * 消费者测试类:接受消息
 */
public class Consumer {
    //接收消息的队列名
    public static final String QUEUE_NAME = "hello";

    //接收消息
    public static void main(String[] args) throws Exception {
        //创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.56.10");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("123");

        //函数式接口的回调函数
        DeliverCallback deliverCallback = (consumerTag,message)->{
            System.out.println(new String(message.getBody()));
        };
        //取消消息时的回调
        CancelCallback cancelCallback = consumerTag->{
            System.out.println("消息消费被中断");
        };

        //创建连接
        Connection connection = connectionFactory.newConnection();
        //建立信道
        Channel channel  = connection.createChannel();
        /**
         * 消费消息
         * 参数1：要消费的队列名
         * 参数2：消费成功之后是否自动答复（true代表自动答复，false代表手动答复）
         * 参数3：消费者未成功消息的回调
         * 参数4：消费者取消消息的回调
         */
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
