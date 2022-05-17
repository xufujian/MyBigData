package com.base;

import org.apache.flink.api.common.JobExecutionResult;

/**
 * @author: 今天风很大
 * @date:2022/5/17 23:07
 * @Description:
 */
public interface BaseJob {
    void init(String[] args) throws Exception;
    JobExecutionResult execute() throws Exception;
}
