package com.atguigu.rabbitmq.four;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

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
        //ConfirmMessage.publishMessageBatch();
        //3：异步确认
        ConfirmMessage.publishMessageSync(); //发送1000条耗时29ms

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
            if (flag) {
                System.out.println("消息发送成功" + i);
            }
        }
        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("单独确认发送" + MESSAGE_COUNT + "条耗时" + (end - begin) + "ms");
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
            if (i % confirmSize == 0) {
                boolean flag = channel.waitForConfirms();
                if (flag) {
                    System.out.println("第" + i + "条前的数据发送成功");
                }
            }
        }
        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("批量确认发送" + MESSAGE_COUNT + "条耗时" + (end - begin) + "ms");
    }

    /**
     * 异步确认
     */
    private static void publishMessageSync() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //队列的声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        //开启发布确认
        channel.confirmSelect();
        /**
         * 线程安全有序的一个哈希表（map） 适用于高并发的情况下
         * 1:轻松的将序号和消息进行关联
         * 2：轻松批量删除条目，只要给到序号
         * 3：支持高并发（多线程）
         */
        ConcurrentSkipListMap<Long,String> outstandingConfirms = new ConcurrentSkipListMap<>();

        //开始时间
        long begin = System.currentTimeMillis();

        //监听成功的消息
        ConfirmCallback ackCallBack = (deleliveryTag, multiple) -> {
            //2：删除成功回调确认的消息
            if(multiple){
                //如果是批量确认这样批量删除（一般为了防止消息丢失不会去批量删除）
                ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(deleliveryTag);
                confirmed.clear();
            }
            outstandingConfirms.remove(deleliveryTag);
            //deleliveryTag即为消息的标记号
            System.out.println("确认的消息:" + deleliveryTag);
        };

        //监听失败的消息
        ConfirmCallback nackCallBack = (deleliveryTag, multiple) -> {
            //打印未确认的消息
            String unconfirmMessage = outstandingConfirms.get(deleliveryTag);
            System.out.println("未确认的消息:" + unconfirmMessage+deleliveryTag);
        };

        /**
         * 异步确认监听器
         * 参数1：成功的消息
         * 参数2：失败的消息
         */
        channel.addConfirmListener(ackCallBack, nackCallBack);
        //批量发送消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            //1：此处记录下所有要发送的消息(key：value形式)
            outstandingConfirms.put(channel.getNextPublishSeqNo(),message);
        }
        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("异步确认发送" + MESSAGE_COUNT + "条耗时" + (end - begin) + "ms");
    }


}
