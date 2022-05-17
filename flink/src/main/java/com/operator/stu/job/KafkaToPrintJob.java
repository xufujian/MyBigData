package com.operator.stu.job;

import com.constant.ParameterConfig;
import com.mybigdata.url.ResourceUrlUtils;
import com.operator.stu.operator.CustomFilterFunction;
import com.operator.stu.utils.FlinkUtils;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

/**
 * @author: 今天风很大
 * @date:2021/7/11 15:53
 * @Description:
 */
public class KafkaToPrintJob {
    public static void main(String[] args) throws Exception {
        ParameterTool parameters = ParameterTool
                .fromPropertiesFile(ResourceUrlUtils.getCurrentModuleConfigUrl(KafkaToPrintJob.class, "config.properties"));

//        DataStream<String> lines = FlinkUtils.createKafkaStream(parameters, SimpleStringSchema.class);
        String sourceTopic = parameters.get(ParameterConfig.sourceTopic);
        String groupId = parameters.get(ParameterConfig.groupId);
        //source
        FlinkKafkaConsumer kafkaConsumer = FlinkUtils.sourceKafka(sourceTopic, groupId, SimpleStringSchema.class, parameters);

        //sink
        String sinkTopic = parameters.get(ParameterConfig.sinkTopic);
        FlinkKafkaProducer<String> kafkaProucuer = FlinkUtils.sinkKafka(sinkTopic, SimpleStringSchema.class, parameters);

        //stream
        DataStreamSource dataStreamSource = FlinkUtils.getEnv().addSource(kafkaConsumer);

        dataStreamSource.filter(new CustomFilterFunction() {

            @Override
            public boolean filter(Object value) throws Exception {
                return false;
            }

            @Override
            public Tuple2<String, String> tp2(String str) {
                return null;
            }

            @Override
            public String getData(String str) {
                return null;
            }

        });

        dataStreamSource.addSink(kafkaProucuer);


        FlinkUtils.getEnv().execute(KafkaToPrintJob.class.getName());
    }
}
