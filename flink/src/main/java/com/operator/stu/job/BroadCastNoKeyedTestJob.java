package com.operator.stu.job;

import com.mybigdata.url.ResourceUrlUtils;
import com.operator.stu.operator.broadcast.MinuteBroadcastSource;
import com.operator.stu.utils.FlinkUtils;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;

/**
 * @author: 今天风很大
 * @date:2021/8/1 18:07
 * @Description: broadcaststream 的学习与使用
 */
public class BroadCastNoKeyedTestJob {
    private static Logger logger = LoggerFactory.getLogger(BroadCastNoKeyedTestJob.class);

    public static void main(String[] args) throws Exception {

        MapStateDescriptor<String, String> Config = new MapStateDescriptor<>("wordsConfig",
                BasicTypeInfo.STRING_TYPE_INFO
                , BasicTypeInfo.STRING_TYPE_INFO);

        ParameterTool parameters = ParameterTool
                .fromPropertiesFile(ResourceUrlUtils.getCurrentModuleConfigUrl(KafkaToPrintJob.class, "config.properties"));

        DataStreamSource<String> dataStreamSource = FlinkUtils.createSocketStream(parameters, "192.168.80.5", 8888);

        StreamExecutionEnvironment env = FlinkUtils.getEnv();
        BroadcastStream<Tuple2<Integer, String>> broadcastStream = env.addSource(new MinuteBroadcastSource()).setParallelism(1).broadcast(Config);


        //要关联已经广播出去的数据
        dataStreamSource.connect(broadcastStream).process(new BroadcastProcessFunction<String, Tuple2<Integer, String>, Object>() {
            // //处理主流数据
            @Override
            public void processElement(String value, ReadOnlyContext ctx, Collector<Object> out) throws Exception {
                String[] sp = value.split(",");
                String name = sp[0];
                String id = sp[1];
                ReadOnlyBroadcastState<String, String> broadcastState = ctx.getBroadcastState(Config);

                String addr = broadcastState.get(id);

                logger.info("关联后的结果为:" + Tuple3.of(name, id, addr));
                out.collect(Tuple3.of(name, id, addr));
            }

            //每来一条规则数据，就添加到内存
            @Override
            public void processBroadcastElement(Tuple2<Integer, String> value, Context ctx, Collector<Object> out) throws Exception {
                int id = value.f0;
                String addr = value.f1;
                BroadcastState<String, String> broadcastState = ctx.getBroadcastState(Config);
                if ("3".equals("id")) {
                    //修改每一个slot中的数据 ！！！
                    broadcastState.remove("3");
                } else {
                    // 将广播数据保存到Context：此时将记录进checkpoint中   此记录会保存在每一个 task slot中,因为taskmanager内的task slot是内存隔离的，所以一个
                    //taskmanager中会存多个
                    broadcastState.put(String.valueOf(id), addr);
                    logger.info("规则数据为:" + Tuple2.of(id, addr));
                }

                Iterator<Map.Entry<String, String>> iterator = broadcastState.iterator();

                //每个task slot都会打印
                while (iterator.hasNext()){
                    Map.Entry<String, String> next = iterator.next();

                    System.out.println("key:"+next.getKey()+" : "+"value:"+next.getValue());
                }

            }
        });

        FlinkUtils.getEnv().execute();
    }
}
