package com.operator.stu.job;

import org.apache.flink.api.common.functions.Partitioner;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.configuration.TaskManagerOptions;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import scala.Int;

/**
 * @author: 今天风很大
 * @date:2021/8/25 0:11
 * @Description:
 */
public class partitonCustomJob {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        // 访问http://localhost:8082 可以看到Flink Web UI
        conf.setInteger(RestOptions.PORT, 8082);
        //手动设置启动TaskExecutor的任务插槽的数量
        conf.setInteger(TaskManagerOptions.NUM_TASK_SLOTS, 2);
        // 创建本地执行环境，并行度为2
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment(1, conf);

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //4核8线程 默认8个并行度
        System.out.println("default parallelism:" + env.getParallelism());
        env.setParallelism(6);

        DataStreamSource<Tuple2<Integer, String>> streamSource = env.fromElements(
                Tuple2.of(1, "123"), Tuple2.of(2, "245"),
                Tuple2.of(3, "345"), Tuple2.of(4, "456"),
                Tuple2.of(5, "567"), Tuple2.of(6, "678")
        );

        // 对(Int, String)中的第二个字段使用 MyPartitioner 中的重分布逻辑
        //第二个参数是对数据流哪个字段使用partiton逻辑。
        DataStream<Tuple2<Integer, String>> partitionCustom = streamSource.partitionCustom(new Partitioner<Integer>() {
            /**
             *
             * @param key 表示要对那个字段进行数据的重分配，我这里用的是Tuple2里面的integer
             * @param numPartitions 表示当前的总并行度
             * @return 返回一个int，表示将此元素下发到下游那个实例上面去
             */
            @Override
            public int partition(Integer key, int numPartitions) {
                //直接按照Tuple2里面的int，下发到下游对应的实例中
                System.out.println("numPartitions:" + numPartitions);
                return key - 1;
            }
        }, 0);

        partitionCustom.print();

        env.execute(partitonCustomJob.class.getName());
    }
}
