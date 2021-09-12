package com.atguigu.rabbitmq.utils;

/**
 * @author huajieli
 * @create 2021-07-25-22:47
 * 线程睡眠工具类
 */
public class SleepUtils {
    public static void sleep(int second){
        try {
            Thread.sleep(1000*second);
        } catch (InterruptedException _ignored) {
            Thread.currentThread().interrupt();
        }
    }
}