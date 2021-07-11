package com.kafka.base;

import com.kafka.utils.KafkaConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: 今天风很大
 * @date:2021/7/4 17:22
 * @Description:
 */
public class kafkaDataProducer {
    private static final Logger logger = LoggerFactory.getLogger(kafkaDataProducer.class);

    public static void main(String[] args) {
        String topic = "t12";
        KafkaProducer kafkaProducer = KafkaConfig.producerProps(topic);
        String json = "dataaa";
        ProducerRecord<String, String> kvProducerRecord = new ProducerRecord<>(topic, String.valueOf(json.hashCode()), json);
        kafkaProducer.send(kvProducerRecord);
    }
}
