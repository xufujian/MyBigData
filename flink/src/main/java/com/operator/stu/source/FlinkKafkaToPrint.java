package com.operator.stu.source;

import com.mybigdata.url.ResourceUrlUtils;
import com.operator.stu.utils.FlinkUtils;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: 今天风很大
 * @date:2021/7/4 16:22
 * @Description:
 */
public class FlinkKafkaToPrint {
    private static final Logger logger = LoggerFactory.getLogger(FlinkKafkaToPrint.class);

    public static void main(String[] args) throws Exception {
        logger.info("start...");
        ParameterTool parameters = ParameterTool
                .fromPropertiesFile(ResourceUrlUtils.getCurrentModuleConfigUrl(FlinkKafkaToPrint.class, "config.properties"));


        DataStream<String> lines = FlinkUtils.createKafkaStream(parameters, SimpleStringSchema.class);
        lines.print();
        FlinkUtils.getEnv().execute(FlinkKafkaToPrint.class.getName());


    }
}
