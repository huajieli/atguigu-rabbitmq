package com.atguigu.rabbitmq.seven;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

/**
 * @author huajieli
 * @create 2021-09-12 22:40
 * topic交换机
 *
 * 消费者02
 */
public class ReceiveLogsTopic02 {
    /**
     *交换机名称
     */
    public static final String EXCHANGE_NAME = "topic_logs";
    /**
     *队列名称
     */
    public static final String QUEUE_NAME = "Q2";
    /**
     *routingKey
     */
    public static final String ROUTING_KEY1 = "*.*.rabbit";
    public static final String ROUTING_KEY2 = "lazy.#";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        /**
         * 声明队列1
         * 参数1：队列名
         * 参数2：是否持久化
         * 参数3：是否共享
         * 参数4：是否自动删除
         * 参数5：其他参数
         */
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //绑定key1
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,ROUTING_KEY1);
        //绑定key2
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,ROUTING_KEY2);
        System.out.println("ReceiveLogsTopic02等待接受消息。。。。");
        //消费消息
        //接受消息时的回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLogsDirect02控制台打印接收到的消息：" + new String(message.getBody(), StandardCharsets.UTF_8));
            System.out.println("接受队列："+QUEUE_NAME+"绑定的routingKey为："+message.getEnvelope().getRoutingKey());
        };
        //取消消息时的回调
        CancelCallback cancelCallback = consumerTag->{
            System.out.println("消息消费被中断");
        };
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
