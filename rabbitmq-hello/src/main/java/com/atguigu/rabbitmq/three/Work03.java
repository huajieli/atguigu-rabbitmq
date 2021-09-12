package com.atguigu.rabbitmq.three;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.atguigu.rabbitmq.utils.SleepUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author huajieli
 * @create 2021-07-25-22:31
 *
 * 消息在手动应答时不丢失，放回队列重新消费
 */
public class Work03 {
    public static final String TASK_QUEUE_NAME = "ack_queue";

    //接受消息
    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("C1等待接收消息处理时间短");
        //函数式接口的回调函数
        DeliverCallback deliverCallback = (consumerTag, message)->{
            //沉睡1s
            SleepUtils.sleep(1);
            System.out.println("接收到的消息"+new String(message.getBody(),"utf-8"));
            //手动应答
            /**
             * 1:参数的标记 tag
             * 2:是否批量应答 false:不批量应答信道中的数据
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };
        //取消消息时的回调
        CancelCallback cancelCallback = consumerTag->{
            System.out.println("消息消费被中断");
        };

        boolean autoAck = false;
        channel.basicConsume(TASK_QUEUE_NAME,autoAck,deliverCallback,cancelCallback);
    }
}
