package com.operator.stu.job;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.configuration.TaskManagerOptions;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: 今天风很大
 * @date:2021/8/22 22:32
 * @Description:
 */
public class TestJob {
    private static Logger logger = LoggerFactory.getLogger(TestJob.class);

    public static void main(String[] args) throws Exception{

        Configuration conf = new Configuration();
        // 访问http://localhost:8082 可以看到Flink Web UI
        conf.setInteger(RestOptions.PORT, 8082);
        //手动设置启动TaskExecutor的任务插槽的数量
        conf.setInteger(TaskManagerOptions.NUM_TASK_SLOTS, 2);
        // 创建本地执行环境，并行度为2
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment(1, conf);
        env.disableOperatorChaining();
        DataStreamSource<String> stream = env.socketTextStream("192.168.80.5", 8888);

        stream.flatMap(new FlatMapFunction<String, Object>() {

            @Override
            public void flatMap(String value, Collector<Object> out) throws Exception {

                out.collect(value);
            }
        });

        stream.map(new MapFunction<String, Object>() {
            @Override
            public Object map(String value) throws Exception {
                return null;
            }
        });



        stream.print();

        env.execute(TestJob.class.getName());
    }
}
