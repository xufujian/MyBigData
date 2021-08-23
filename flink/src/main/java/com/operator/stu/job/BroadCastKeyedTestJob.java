package com.operator.stu.job;

import com.mybigdata.model.Student;
import com.mybigdata.url.ResourceUrlUtils;
import com.operator.stu.operator.broadcast.MinuteBroadcastSource;
import com.operator.stu.utils.FlinkUtils;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.streaming.api.functions.co.KeyedBroadcastProcessFunction;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

/**
 * @author: 今天风很大
 * @date:2021/8/1 18:07
 * @Description: broadcaststream 的学习与使用
 */
public class BroadCastKeyedTestJob {
    private static Logger logger = LoggerFactory.getLogger(BroadCastKeyedTestJob.class);

    public static void main(String[] args) throws Exception {

        MapStateDescriptor<String, String> Config = new MapStateDescriptor<>("wordsConfig",
                BasicTypeInfo.STRING_TYPE_INFO
                , BasicTypeInfo.STRING_TYPE_INFO);

        ParameterTool parameters = ParameterTool
                .fromPropertiesFile(ResourceUrlUtils.getCurrentModuleConfigUrl(KafkaToPrintJob.class, "config.properties"));

        DataStreamSource<String> dataStreamSource = FlinkUtils.createSocketStream(parameters, "192.168.80.5", 8888);

        StreamExecutionEnvironment env = FlinkUtils.getEnv();
        BroadcastStream<Tuple2<Integer, String>> broadcastStream = env.addSource(new MinuteBroadcastSource()).setParallelism(1).broadcast(Config);


        dataStreamSource.keyBy("1").connect(broadcastStream).process(new KeyedBroadcastProcessFunction<Object, String, Tuple2<Integer, String>, Object>() {
            @Override
            public void processElement(String value, ReadOnlyContext ctx, Collector<Object> out) throws Exception {

            }

            @Override
            public void processBroadcastElement(Tuple2<Integer, String> value, Context ctx, Collector<Object> out) throws Exception {

            }
        });

        FlinkUtils.getEnv().execute();
    }
}
