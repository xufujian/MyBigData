package com.job;

import com.base.AbstractBaseJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: 今天风很大
 * @date:2022/5/17 23:25
 * @Description:
 */
public class TestStateJob extends AbstractBaseJob {
    private static final Logger logger
            = LoggerFactory.getLogger(TestStateJob.class);

    @Override
    protected void process() throws Exception {

    }

    public static void main(String[] args) throws Exception {
        TestStateJob stateJob = new TestStateJob();
        stateJob.init(args);
        stateJob.execute();
    }
}
