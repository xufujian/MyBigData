package com.core.integration;

import com.base.AbstractBaseJob;
import com.constant.ParameterConfig;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.Objects;
import java.util.Properties;

/**
 * @author: 今天风很大
 * @date:2022/5/17 23:28
 * @Description:
 */
public class KafkaBuilder {
    private static final String COMPRESSION_TYPE = "snappy";

    public static Properties userKafkaConfigure(AbstractBaseJob baseJob, String topic, String groupId) {
        return baseJob.setUserKafkaConfig(topic, groupId);
    }

    public static void addUserProperties(AbstractBaseJob baseJob, String topic, String groupId, Properties properties) {
        Properties userProperties = userKafkaConfigure(baseJob, topic, groupId);
        if (Objects.nonNull(userProperties)) {
            userProperties.keySet().forEach(t -> {
                properties.put(t, userProperties.get(t));
            });
        }
    }

    public static <T> FlinkKafkaConsumer<T> sourceFatKafka(String topic, String groupId, KafkaDeserializationSchema<T> deserialization, AbstractBaseJob baseJob) {
        Properties properties = new Properties();
        String serverAddress = baseJob.getParameter().get(ParameterConfig.sourceBootstrapServer);
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, serverAddress);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        addUserProperties(baseJob, topic, groupId, properties);
        FlinkKafkaConsumer<T> consumer = new FlinkKafkaConsumer<>(topic, deserialization, properties);
        setKafkaConsumerOffset(baseJob,consumer);
        return consumer;
    }

    public static void setKafkaConsumerOffset(AbstractBaseJob baseJob, FlinkKafkaConsumer consumer) {
        long startFromTimeStamp = baseJob.getParameter().getLong(ParameterConfig.startFromTimeStamp);
        if (Objects.nonNull(startFromTimeStamp) && !Objects.equals(startFromTimeStamp, -1l)) {
            consumer.setStartFromTimestamp(startFromTimeStamp);
        } else {
            consumer.setStartFromGroupOffsets();
        }

    }
}
