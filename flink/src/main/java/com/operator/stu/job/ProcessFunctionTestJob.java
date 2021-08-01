package com.operator.stu.job;

import com.mybigdata.url.ResourceUrlUtils;
import com.operator.stu.utils.FlinkUtils;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: 今天风很大
 * @date:2021/8/1 23:04
 * @Description:
 */
public class ProcessFunctionTestJob {
    private static Logger logger = LoggerFactory.getLogger(ProcessFunctionTestJob.class);

    public static void main(String[] args) throws Exception {
        MapStateDescriptor<String, String> Config = new MapStateDescriptor<>("wordsConfig",
                BasicTypeInfo.STRING_TYPE_INFO
                , BasicTypeInfo.STRING_TYPE_INFO);

        ParameterTool parameters = ParameterTool
                .fromPropertiesFile(ResourceUrlUtils.getCurrentModuleConfigUrl(KafkaToPrintJob.class, "config.properties"));

        DataStreamSource<String> dataStreamSource = FlinkUtils.createSocketStream(parameters, "192.168.80.5", 8888);


        FlinkUtils.getEnv().execute();
    }
}
