package com.kafka.utils;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Arrays;
import java.util.Properties;

/**
 * @author: 今天风很大
 * @date:2021/7/4 17:26
 * @Description:
 */
public class KafkaConfig {
    public static KafkaProducer producerProps(String topic) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.80.5:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("key.serializer", StringSerializer.class.getName());
        props.put("value.serializer", StringSerializer.class.getName());
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        NewTopic newTopic = new NewTopic(topic, 3, (short) 1);
        AdminClient adminClient = KafkaAdminClient.create(props);
        adminClient.createTopics(Arrays.asList(newTopic));
        adminClient.close();
        return producer;
    }

    public static KafkaConsumer consumerProps(String topic) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.80.5:9092");
        props.put("group.id", "tt12");
        props.put("auto.offset.reset", "latest");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        //设置kay,value的序列化
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(props);
        kafkaConsumer.subscribe(Arrays.asList(topic));
        return kafkaConsumer;
    }

    public static void main(String[] args) {
        System.out.println(StringSerializer.class.getName());

        System.out.println(StringDeserializer.class.getName());
    }
}
