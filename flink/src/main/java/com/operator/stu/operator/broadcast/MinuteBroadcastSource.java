package com.operator.stu.operator.broadcast;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Random;

/**
 * @author: 今天风很大
 * @date:2021/8/1 18:09
 * @Description:
 */
public class MinuteBroadcastSource extends RichParallelSourceFunction<Tuple2<Integer, String>> {
    private volatile boolean isRunning;
    private volatile int lastUpdateMin = -1;

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        isRunning = true;
    }

    @Override
    public void run(SourceContext<Tuple2<Integer, String>> ctx) throws Exception {
        while (isRunning) {
            ctx.collect(Tuple2.of(new Random().nextInt(10), new String("bc"+new Random().nextInt(100))));
            Thread.sleep(10000);
        }
    }

    @Override
    public void cancel() {
        isRunning = false;
    }
}
