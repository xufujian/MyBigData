package com.operator.stu.job.window;

import com.mybigdata.url.ResourceUrlUtils;
import com.operator.stu.job.KafkaToPrintJob;
import com.operator.stu.utils.FlinkUtils;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.configuration.TaskManagerOptions;
import org.apache.flink.streaming.api.datastream.*;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import scala.Int;

/**
 * @author: 今天风很大
 * @date:2021/9/1 23:26
 * @Description: countwindow 的使用
 * 分组后，在调用CountWindow，每个组达到一定的条数才出发任务执行
 */
public class CountWindowJob {
    public static void main(String[] args) throws Exception{
        Configuration conf = new Configuration();
        // 访问http://localhost:8082 可以看到Flink Web UI
        conf.setInteger(RestOptions.PORT, 8082);
        //手动设置启动TaskExecutor的任务插槽的数量
        conf.setInteger(TaskManagerOptions.NUM_TASK_SLOTS, 2);
        ParameterTool parameters = ParameterTool
                .fromPropertiesFile(ResourceUrlUtils.getCurrentModuleConfigUrl(KafkaToPrintJob.class, "config.properties"));
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment(1, conf);
        DataStreamSource<String> dataStreamSource = env.socketTextStream("192.168.80.5", 8888);

//        DataStreamSource<String> dataStreamSource = FlinkUtils.createSocketStream(parameters, "192.168.80.5", 8888);
        //socket,1,spark,2,hive,1

        SingleOutputStreamOperator<Tuple2<String,Integer>> nums = dataStreamSource.map(new MapFunction<String, Tuple2<String,Integer>>() {
            @Override
            public Tuple2<String,Integer> map(String value) throws Exception {
                String[] split = value.split(",");
                String name=split[0];
                Integer num=Integer.parseInt(split[1]);
                return Tuple2.of(name,num);
            }
        });

        //不分组，将整体当成一个组
//        AllWindowedStream<Integer, GlobalWindow> allWindowedStream = nums.countWindowAll(5);

        //这里先分组，在划分窗口
        KeyedStream<Tuple2<String, Integer>, Tuple> keyedStream = nums.keyBy(0);
        //划分窗口 5条算一个窗口
        WindowedStream<Tuple2<String, Integer>, Tuple, GlobalWindow> window = keyedStream.countWindow(5);
        SingleOutputStreamOperator<Tuple2<String, Integer>> sumed = window.sum(1);

        //在窗口中聚合
//        SingleOutputStreamOperator<Integer> sumed = allWindowedStream.sum(0);
        sumed.print();

        env.execute(CountWindowJob.class.getName());
    }
}
