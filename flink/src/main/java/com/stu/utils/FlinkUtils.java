package com.stu.utils;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @author: 今天风很大
 * @date:2021/7/4 15:23
 * @Description:
 */
public class FlinkUtils {
    private static StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    public static <T> DataStream<T> createKafkaStream(ParameterTool parameters, Class<? extends DeserializationSchema<T>> clazz) throws IllegalAccessException, InstantiationException {
        //开启checkpointing，同事开启重启策略
        env.enableCheckpointing(parameters.getLong("checkpoint.interval", 50000l));
        //取消任务checkpoint不删除
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", parameters.getRequired("bootstrap.servers"));
        props.setProperty("group.id", parameters.getRequired("group.id"));
        //如果没有记录偏移量，第一次从最开始消费
        props.setProperty("auto.offset.reset", parameters.get("auto.offset.reset", "earliest"));
        //kafka 的消费这不自动提交偏移量
        props.setProperty("enable.auto.commit", parameters.get("enable.auto.commit", "false"));
        String topics = parameters.get("topics");
        List<String> topicList = Arrays.asList(topics.split(","));
        FlinkKafkaConsumer<T> kafkaConsumer = new FlinkKafkaConsumer<T>(
                topicList,
                clazz.newInstance(),
                props);
        // 默认ture
        // kafkaConsumer.setCommitOffsetsOnCheckpoints(true);
        return env.addSource(kafkaConsumer);
    }

    public static StreamExecutionEnvironment getEnv(){
        return env;
    }

        public static void main(String[] args) throws IOException {
        ParameterTool parameterTool = ParameterTool.fromPropertiesFile(args[0]);

        String groupId = parameterTool.get("group.id");
        System.out.println(groupId);
    }
}
