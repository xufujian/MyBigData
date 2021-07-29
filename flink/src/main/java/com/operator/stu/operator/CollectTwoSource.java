package com.operator.stu.operator;

import org.apache.flink.streaming.api.functions.co.CoProcessFunction;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: 今天风很大
 * @date:2021/7/29 22:29
 * @Description:
 */
public class CollectTwoSource  extends CoProcessFunction {
    private static Logger logger = LoggerFactory.getLogger(CollectTwoSource.class);

    @Override
    public void processElement1(Object value, Context ctx, Collector out) throws Exception {

        logger.info("1号流的数据是:"+value.toString());

    }

    @Override
    public void processElement2(Object value, Context ctx, Collector out) throws Exception {

        logger.info("2号流的数据是:"+value.toString());
    }
}
