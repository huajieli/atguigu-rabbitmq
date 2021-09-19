package com.atguigu.rabbitmq.eigth;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author huajieli
 * @create 2021-09-19 10:25
 * 死信队列
 * 消费者01
 */
public class Consumer01 {
    /**
     * 普通交换机
     */
    private static final String NORMAL_EXCHANGE = "normal_exchange";
    /**
     * 死信交换机
     */
    private static final String DEAD_EXCHANGE = "dead_exchange";
    /**
     * 普通队列
     */
    private static final String NORMAL_QUEUE = "normal_queue";
    /**
     * 死信队列
     */
    private static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //声明死信交换机和普通交换机
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        //声明死信队列和普通队列
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);
        /**
         * 注意普通队列的最后一个参数用来建立和死信交换机的关系（消息成为死信后转发到-》死信交换机-》死信队列）
         * 1:x-dead-letter-exchange(死信交换机)
         * 2:x-dead-letter-routing-key（死信队列）
         */
        Map<String, Object> mapParams = new HashMap<>();
        mapParams.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        mapParams.put("x-dead-letter-routing-key", "lisi");
        //队列中存放消息的个数,当队列中积压超过6个后,会进入死信队列
        mapParams.put("x-max-length",6);
        channel.queueDeclare(NORMAL_QUEUE, false, false, false, mapParams);

        //普通队列绑定普通交换机
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "zhangsan");
        //死信队列绑定死信交换机
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");

        System.out.println("Consumer01等待接受消息，会把接收消息打印在下面的屏幕上......");

        //接受消息时的回调
       /* DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("控制台Consumer01打印接收到的消息：" + new String(message.getBody(), StandardCharsets.UTF_8));
        };*/
        //消息因拒绝而进入到死信队列
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String msg = new String(message.getBody(), StandardCharsets.UTF_8);
            if(msg.equals("info5")){
                channel.basicReject(message.getEnvelope().getDeliveryTag(),false);
                System.out.println("被拒绝的消息：" + new String(message.getBody(), StandardCharsets.UTF_8));
            }
            System.out.println("控制台Consumer01打印接收到的消息：" + new String(message.getBody(), StandardCharsets.UTF_8));
        };
        //取消消息时的回调
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("消息消费被中断");
        };
        //channel.basicConsume(NORMAL_QUEUE, true, deliverCallback, cancelCallback);
        //不可开启自动应答（第二个参数设置成false）
        channel.basicConsume(NORMAL_QUEUE, false, deliverCallback, cancelCallback);
    }

}
