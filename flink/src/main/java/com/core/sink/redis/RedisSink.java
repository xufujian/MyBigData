package com.core.sink.redis;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import redis.clients.jedis.Jedis;

/**
 * @author: 今天风很大
 * @date:2021/7/27 22:50
 * @Description:
 */
public class RedisSink extends RichSinkFunction<Tuple3<String, String, String>> {
    private transient Jedis jedis;

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        jedis = new Jedis("127.0.0.1", 6379, 5000, 1000);
        jedis.select(1);
    }

    @Override
    public void invoke(Tuple3<String, String, String> value, Context context) throws Exception {
        if (!jedis.isConnected()) {
            jedis.connect();
        }
        jedis.hset(value.f0, value.f1, value.f2);
    }

    @Override
    public void close() throws Exception {
        super.close();
        jedis.close();
    }
}
