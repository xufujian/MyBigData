package com.base;

import com.constant.ParameterConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Properties;

/**
 * @author: 今天风很大
 * @date:2022/5/17 23:07
 * @Description:
 */
public abstract class AbstractBaseJob implements BaseJob {
    private final StreamExecutionEnvironment env;
    private ParameterTool parser;

    public AbstractBaseJob() {
        this.env = StreamExecutionEnvironment.getExecutionEnvironment();
    }

    @Override
    public void init(String[] args) throws Exception {
        try {
            ParameterTool parser = ParameterTool.fromArgs(args);
            this.parser = parser;
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }
    }

    private void initEvn() {

    }

    protected void setDefaultEnvConfig(StreamExecutionEnvironment env) {
        env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
        env.enableCheckpointing(60000);
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        env.getCheckpointConfig().setCheckpointTimeout(10 * 60 * 1000);
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(1000);
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
        env.getCheckpointConfig().setPreferCheckpointForRecovery(true);
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
        env.getCheckpointConfig().setFailOnCheckpointingErrors(false);
        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(10, 10000));
        env.getConfig().setGlobalJobParameters(getParameter());
    }

    protected void overrideDefaultEnvConfig(StreamExecutionEnvironment env) {
        //do something
    }

    public Properties setUserKafkaConfig(String topic, String groupId) {
        return null;
    }

    protected abstract void process() throws Exception;

    @Override
    public JobExecutionResult execute() throws Exception {
        this.process();
        String jobName = this.parser.get(ParameterConfig.jobName);
        if (StringUtils.isNotBlank(jobName)) {
            return this.env.execute(jobName);
        } else {
            return this.env.execute(this.getClass().getSimpleName());
        }
    }

    protected StreamExecutionEnvironment getEnv() {
        return this.env;
    }

    public ParameterTool getParameter() {
        return this.parser;
    }
}
