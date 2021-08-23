package com.operator.stu.job;

import com.mybigdata.model.Score;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.configuration.TaskManagerOptions;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * @author: 今天风很大
 * @date:2021/8/23 23:42
 * @Description:
 */
public class ReduceJob {
    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();
        // 访问http://localhost:8082 可以看到Flink Web UI
        conf.setInteger(RestOptions.PORT, 8082);
        //手动设置启动TaskExecutor的任务插槽的数量
        conf.setInteger(TaskManagerOptions.NUM_TASK_SLOTS, 2);
        // 创建本地执行环境，并行度为2
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment(1, conf);
        env.disableOperatorChaining();
        DataStreamSource<Score> dataStream = env.fromElements(
                Score.of("xufujian", "math", 99),
                Score.of("xufujian", "english", 96),
                Score.of("xufujian","tqd",43),
                Score.of("xulili", "math", 87),
                Score.of("xulili", "english", 93)
        );

        SingleOutputStreamOperator<Score> reduceStream = dataStream.keyBy(Score::getName)
                .reduce(new ReduceFunction<Score>() {
                    @Override
                    public Score reduce(Score s1, Score s2) throws Exception {
                        return Score.of(s1.getName(), "Sum", s1.getScore() + s2.getScore());
                    }
                });
        reduceStream.print();

        env.execute(TestJob.class.getName());
    }
}
