package com.stu.partition.partitionStu;

import com.stu.source.MySimpleCustomSource;
import com.stu.utils.FlinkUtils;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple1;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author: 今天风很大
 * @date:2021/7/19 23:06
 * @Description:
 */
public class StreamingDemoWithMyPartiton {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = FlinkUtils.getEnv();
        env.setParallelism(3);
        DataStreamSource<Long> text = env.addSource(new MySimpleCustomSource());
        // 对数据进行转换，把long类型的转换成tuple类型
        DataStream<Tuple1<Long>> tupData = text.map(new MapFunction<Long, Tuple1<Long>>() {
            public Tuple1<Long> map(Long aLong) throws Exception {
                return new Tuple1<Long>(aLong);
            }
        });
        //分区之后的数据  //partition分区，按照第一个字段进行分区
        DataStream<Tuple1<Long>> partData = tupData.partitionCustom(new MyPartition(), 0);
        SingleOutputStreamOperator<Long> maps = partData.map(new MapFunction<Tuple1<Long>, Long>() {
            public Long map(Tuple1<Long> value) throws Exception {
                System.out.println("获取当前线程id: " + Thread.currentThread().getId() + ",value" + value);
                return value.getField(0);
            }
        });

        maps.print().setParallelism(1);
        env.execute("own definite partiotn");

    }
}
