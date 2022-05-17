package com.operator.stu.job;

import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.accumulators.IntCounter;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.configuration.TaskManagerOptions;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * @author: 今天风很大
 * @date:2021/8/26 23:55
 * @Description: flink 累加器的使用
 */
public class AccumulatorJob {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        // 访问http://localhost:8082 可以看到Flink Web UI
        conf.setInteger(RestOptions.PORT, 8082);
        //手动设置启动TaskExecutor的任务插槽的数量
        conf.setInteger(TaskManagerOptions.NUM_TASK_SLOTS, 2);
        // 创建本地执行环境，并行度为2
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment(1, conf);

//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //4核8线程 默认8个并行度
        System.out.println("default parallelism:"+env.getParallelism());

        DataStreamSource<String> dataStream = env.fromElements("this is hello world");

        dataStream.flatMap(new RichFlatMapFunction<String, String>() {
           private   IntCounter numOfLines = new IntCounter(0);

            @Override
            public void open(Configuration parameters) throws Exception {
                super.open(parameters);
                // 在RuntimeContext中注册累加器
                getRuntimeContext().addAccumulator("num-of-lines", this.numOfLines);
            }
            @Override
            public void flatMap(String value, Collector<String> out) throws Exception {
                // 运行过程中调用累加器
                this.numOfLines.add(1);
                out.collect(value);
            }
        });

        JobExecutionResult execute = env.execute();
//        JobExecutionResult jobExecutionResult = execute.getJobExecutionResult();
//
//        Integer accumulatorResult = jobExecutionResult.getAccumulatorResult("num-of-lines");
//        System.out.println("num of lines:"+accumulatorResult);
    }
}
