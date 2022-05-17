package com.constant;

/**
 * @author: 今天风很大
 * @date:2021/7/11 16:33
 * @Description:
 */
public interface ParameterConfig {
    String COMPRESSION_TYPE="snappy";
    String sourceBootstrapServer = "bootstrap.servers";
    String sinkBootstrapServer = "bootstrap.servers";

    //source
    String sourceTopic="sourceTopic";
    String groupId="group.id";

    //sink
    String sinkTopic="sinkTopic";

    /**
     * job name
     */
    String jobName="jobName";

    String startFromTimeStamp="startFromTimeStamp";
}
