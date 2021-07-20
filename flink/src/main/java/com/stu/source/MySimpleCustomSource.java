package com.stu.source;

import org.apache.flink.api.java.tuple.Tuple1;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.Random;

/**
 * @author: 今天风很大
 * @date:2021/7/19 23:28
 * @Description:
 */
public class MySimpleCustomSource implements SourceFunction<Long> {
    private static final int BOUND = 10;
    private static final long serialVersionUID = 1L;

    private Random rnd = new Random();

    private volatile boolean isRunning = true;
    private int counter = 0;


    @Override
    public void run(SourceContext<Long> ctx) throws Exception {
        while (isRunning && counter < BOUND) {
            int first = rnd.nextInt(10);
            int second = rnd.nextInt(10);
            ctx.collect(Long.valueOf(first));
            counter++;
            Thread.sleep(50l);
        }
    }

    @Override
    public void cancel() {
        isRunning = false;
    }
}
