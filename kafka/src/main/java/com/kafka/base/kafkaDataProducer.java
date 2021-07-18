package com.kafka.base;

import cn.hutool.json.JSONUtil;
import com.kafka.utils.KafkaConfig;
import com.kafka.utils.KafkaData;
import com.mybigdata.model.Student;
import org.apache.kafka.clients.producer.KafkaProducer;
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
//        creatTopic("t12sink");
        String topic = "t12";
        KafkaProducer kafkaProducer = KafkaConfig.producerProps(topic);
        int i = 0;
        while (i < 10000) {
            i++;
            Student student = KafkaData.producerDataOfStudent();
            String json = JSONUtil.toJsonStr(student);
            ProducerRecord<String, String> kvProducerRecord = new ProducerRecord<>(topic, String.valueOf(json.hashCode()), json);
            kafkaProducer.send(kvProducerRecord);
        }

    }

    public static void creatTopic(String topic) {
        KafkaProducer kafkaProducer = KafkaConfig.producerProps(topic);
    }
}
