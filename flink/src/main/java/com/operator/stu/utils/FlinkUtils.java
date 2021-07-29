package com.operator.stu.utils;

import com.mybigdata.constants.ParameterConfig;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;

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
        //设置全局的参数
        env.getConfig().setGlobalJobParameters(parameters);

        //开启checkpointing，同事开启重启策略
        env.enableCheckpointing(parameters.getLong("checkpoint.interval", 50000l));
        //取消任务checkpoint不删除
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", parameters.getRequired("bootstrap.servers"));
        props.setProperty("group.id", parameters.getRequired("group.id"));
        //如果没有记录偏移量，第一次从最开始消费
        props.setProperty("auto.offset.reset", parameters.get("auto.offset.reset", "lastest"));
        //kafka 的消费这不自动提交偏移量
        props.setProperty("enable.auto.commit", parameters.get("enable.auto.commit", "false"));
        String topics = parameters.get("sourceTopic");
        List<String> topicList = Arrays.asList(topics.split(","));
        FlinkKafkaConsumer<T> kafkaConsumer = new FlinkKafkaConsumer<T>(
                topicList,
                clazz.newInstance(),
                props);
        // 默认ture
        // kafkaConsumer.setCommitOffsetsOnCheckpoints(true);
        return env.addSource(kafkaConsumer);
    }

    public static DataStreamSource<String> createSocketStream(ParameterTool parameters, String host, int port) throws IllegalAccessException, InstantiationException {
        //设置全局的参数
        env.getConfig().setGlobalJobParameters(parameters);

        //开启checkpointing，同事开启重启策略
        env.enableCheckpointing(parameters.getLong("checkpoint.interval", 50000l));
        //取消任务checkpoint不删除
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
        // 默认ture
        // kafkaConsumer.setCommitOffsetsOnCheckpoints(true);
        return env.socketTextStream(host,port);
    }

    //    public static <T> FlinkKafkaConsumer<T> sourceKafka(String topic, String groupId, KafkaDeserializationSchema<T> deserializer,ParameterTool parameterTool){
    public static <T> FlinkKafkaConsumer<T> sourceKafka(String topic, String groupId, Class<? extends DeserializationSchema<T>> clazz, ParameterTool parameterTool) throws IllegalAccessException, InstantiationException {
        Properties properties = new Properties();
        String serverAddress = parameterTool.get(ParameterConfig.sourceBootstrapServer);
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, serverAddress);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        FlinkKafkaConsumer<T> kafkaConsumer = new FlinkKafkaConsumer<>(topic, clazz.newInstance(), properties);
//        kafkaConsumer.setStartFromTimestamp();
        return kafkaConsumer;
    }

    //    public static <T> FlinkKafkaProducer<T> sinkKafka(String topic, SerializationSchema<T> serializationSchema,ParameterTool parameterTool){
    public static <T> FlinkKafkaProducer<T> sinkKafka(String topic, Class<? extends SerializationSchema<T>> clazz, ParameterTool parameterTool) throws IllegalAccessException, InstantiationException {
        Properties properties = new Properties();
        String serverAddress = parameterTool.get(ParameterConfig.sinkBootstrapServer);
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, serverAddress);
        properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, ParameterConfig.COMPRESSION_TYPE);
        properties.put(ProducerConfig.RETRIES_CONFIG, 3);
        return new FlinkKafkaProducer<T>(topic, clazz.newInstance(), properties);
    }


    public static StreamExecutionEnvironment getEnv() {
        return env;
    }

    public static void main(String[] args) throws IOException {
        ParameterTool parameterTool = ParameterTool.fromPropertiesFile(args[0]);
        String groupId = parameterTool.get("group.id");
        System.out.println(groupId);
    }
}
