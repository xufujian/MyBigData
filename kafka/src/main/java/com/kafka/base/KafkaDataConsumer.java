package com.kafka.base;

import com.kafka.utils.KafkaConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Arrays;
import java.util.Properties;

/**
 * @author: 今天风很大
 * @date:2021/7/4 17:33
 * @Description:
 */
public class KafkaDataConsumer {

    public static void main(String[] args) {
        String topic = "t12sink";
        KafkaConsumer kafkaConsumer = KafkaConfig.consumerProps(topic);

        while (true) {
            ConsumerRecords<String, String> poll = kafkaConsumer.poll(100);
            for (ConsumerRecord<String, String> consumerRecords : poll) {
                String value = consumerRecords.value();
                int partition = consumerRecords.partition();
                long timestamp = consumerRecords.timestamp();
                System.out.println("消费的数据时:" + value + " 对应的分区是 >>>" + partition + " 当前时间>>" + timestamp + " 当前线程ID>>" + Thread.currentThread().getId());
            }
        }

    }
}
