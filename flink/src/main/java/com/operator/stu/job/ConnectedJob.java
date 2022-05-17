package com.operator.stu.job;

import com.mybigdata.model.Score;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.configuration.TaskManagerOptions;
import org.apache.flink.shaded.zookeeper3.org.apache.jute.compiler.JString;
import org.apache.flink.streaming.api.datastream.ConnectedStreams;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;
import org.apache.flink.streaming.api.functions.co.KeyedCoProcessFunction;

/**
 * @author: 今天风很大
 * @date:2021/8/24 0:03
 * @Description: connect 可以对两个不同类型的数据源进行连接。
 */
public class ConnectedJob {
    public static void main(String[] args) throws Exception{

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

       env.disableOperatorChaining();
        DataStreamSource<Integer> inStream = env.fromElements(1, 0, 9, 2, 3, 6);

        DataStreamSource<String> stringStream = env.fromElements("LOW", "HIGHT", "LOW", "FAT");

        ConnectedStreams<Integer, String> connectStream = inStream.connect(stringStream);

        SingleOutputStreamOperator<Object> mapStream = connectStream.map(new CoMapFunction<Integer, String, Object>() {
            //数据流1
            @Override
            public String map1(Integer value) throws Exception {
                return value.toString();
            }
            //数据流2
            @Override
            public String map2(String value) throws Exception {
                return value;
            }
        });


        mapStream.print();


        env.execute(TestJob.class.getName());
    }
}
