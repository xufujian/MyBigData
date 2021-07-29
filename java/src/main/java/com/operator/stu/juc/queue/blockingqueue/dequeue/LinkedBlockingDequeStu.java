package com.operator.stu.juc.queue.blockingqueue.dequeue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author: 今天风很大
 * @date:2021/7/15 23:47
 * @Description: 双向阻塞队列
 * @CoreMethod 1.addFirst 队列前部添加元素 返回 void
 * 2.addLast  队列尾部添加元素 返回 void
 * 3.offerFirst 队列前部添加元素 返回bool
 * 4.offerLast  队列前部添加元素 返回bool
 * 5.peekFirst 获取队列头部元素  返回object
 * 6.peekLast  获取队列尾部元素  返回object
 * 7.take=takeFirst
 * 8.takeFirst 队头取数，若队列空，则等待
 * 9.takeLast  队尾取数
 * 10.putFirst 对头添加元素，若队列满，则等待
 * 11.putFirst
 */
public class LinkedBlockingDequeStu {
    private static final Logger logger = LoggerFactory.getLogger(LinkedBlockingDequeStu.class);

    public static void main(String[] args) throws InterruptedException {
        int dequeSize = 4;
        System.out.println("初始化一个大小为：" + dequeSize + "的双端阻塞队列");
        LinkedBlockingDeque lbDeque = new LinkedBlockingDeque(dequeSize); //生产中最好设置一下大小，否则容易造成内存不足
        lbDeque.addFirst("1");
        lbDeque.addLast("10");

        System.out.println("队头元素为:" + lbDeque.getFirst());
        System.out.println("队尾元素为" + lbDeque.getLast());

        System.out.println("往队头插入元素：" + lbDeque.offerFirst("0"));
        System.out.println("取出队头元素:" + lbDeque.peekFirst());
        System.out.println("取出队尾元素:" + lbDeque.peekLast());
        System.out.println("队头插入元素-1");
        lbDeque.putFirst("-1");
        System.out.println("队列大小为:" + lbDeque.size()); //链表 时间复杂度o(n)
//        System.out.println("等待插入数据中...");
//        lbDeque.putFirst("6");

        new Thread(new ThreadRunner(lbDeque), "threadRunner").start();

        //后进先出
        while (!lbDeque.isEmpty()) {
            System.out.print(lbDeque.takeFirst() + ",");
        }

    }

    static class ThreadRunner implements Runnable {
        private LinkedBlockingDeque lbDeque;

        public ThreadRunner(LinkedBlockingDeque lbDeque) {
            this.lbDeque = lbDeque;
        }

        @Override
        public void run() {
            System.out.println("当前线程名称:" + Thread.currentThread().getName());
            System.out.println("另起线程取出队头元素：" + lbDeque.peekFirst());
            System.out.println("另起线程取出队尾元素：" + lbDeque.peekLast());
        }
    }
}
