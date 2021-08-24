package com.operator.stu.job;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.configuration.TaskManagerOptions;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author: 今天风很大
 * @date:2021/8/24 23:46
 * @Description: 数据重分布测试
 */
public class distributionJob {
    public static void main(String[] args)throws Exception{
        Configuration conf = new Configuration();
        // 访问http://localhost:8082 可以看到Flink Web UI
        conf.setInteger(RestOptions.PORT, 8082);
        //手动设置启动TaskExecutor的任务插槽的数量
        conf.setInteger(TaskManagerOptions.NUM_TASK_SLOTS, 2);
        // 创建本地执行环境，并行度为2
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment(1, conf);

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //4核8线程 默认8个并行度
        System.out.println("default parallelism:"+env.getParallelism());

        env.disableOperatorChaining();
        DataStreamSource<Integer> inStream = env.fromElements(1, 0, 9, 2, 3, 6);
        DataStreamSource<String> stringStream = env.fromElements("LOW", "HIGHT", "LOW", "FAT");

//        DataStream<Integer> shuffle = inStream.shuffle();
//
//        shuffle.print();
        inStream.global().print();

        env.execute(distributionJob.class.getName());
    }
}
