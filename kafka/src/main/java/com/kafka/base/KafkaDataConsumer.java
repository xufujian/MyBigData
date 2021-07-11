package com.kafka.base;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

/**
 * @author: 今天风很大
 * @date:2021/7/4 17:33
 * @Description:
 */
public class KafkaDataConsumer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers","192.168.80.5:9092");
        props.put("group.id","tt1");
        props.put("auto.offset.reset","latest");
        props.put("enable.auto.commit","true");
        props.put("auto.commit.interval.ms","1000");
        //设置kay,value的序列化
        props.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(props);
        kafkaConsumer.subscribe(Arrays.asList("t12"));
        while (true) {
            ConsumerRecords<String, String> poll = kafkaConsumer.poll(10000);
            for (ConsumerRecord<String, String> consumerRecords : poll) {
                String value = consumerRecords.value();
                int partition = consumerRecords.partition();
                long timestamp = consumerRecords.timestamp();
                System.out.println("消费的数据时:" + value + " 对应的分区是 >>>" + partition +" 当前时间>>"+timestamp+" 当前线程ID>>"+Thread.currentThread().getId());
            }
        }
    }
}
