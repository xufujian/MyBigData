package com.stu.sink;

import com.core.sink.RedisSink;
import com.stu.partition.partitionStu.MyPartition;
import com.stu.source.MySimpleCustomSource;
import com.stu.utils.FlinkUtils;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple1;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * @author: 今天风很大
 * @date:2021/7/27 22:31
 * @Description: socket to redis   测试redis sink function
 */
public class SocketToRedisSinkTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = FlinkUtils.getEnv();
        env.setParallelism(1);
        DataStreamSource<String> lines = env.socketTextStream("192.168.80.5", 8888);
        SingleOutputStreamOperator<String> words = lines.flatMap(new FlatMapFunction<String, String>() {

            @Override
            public void flatMap(String value, Collector<String> out) throws Exception {
                String[] words = value.split(" ");
                for (String word : words) {
                    out.collect(word);
                }
            }
        });

        SingleOutputStreamOperator<Tuple2<String, Integer>> wordAndOne = words.map(new MapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public Tuple2<String, Integer> map(String value) throws Exception {
                return Tuple2.of(value, 1);
            }
        });
        SingleOutputStreamOperator<Tuple2<String, Integer>> sumed = wordAndOne.keyBy(0).sum(1);

        SingleOutputStreamOperator<Tuple3<String, String, String>> word_count = sumed.map(new MapFunction<Tuple2<String, Integer>, Tuple3<String, String, String>>() {
            @Override
            public Tuple3<String, String, String> map(Tuple2<String, Integer> tp) throws Exception {

                return Tuple3.of("WORD_COUNT", tp.f0, tp.f1.toString());
            }
        });
        word_count.addSink(new RedisSink()).setParallelism(1);
        word_count.print().setParallelism(1);
        env.execute("own definite partiotn");
    }
}
