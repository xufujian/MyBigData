package com.operator.stu.operator;


import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.java.tuple.Tuple2;

import java.io.Serializable;
import java.util.Map;

/**
 * @author: 今天风很大
 * @date:2021/7/30 23:08
 * @Description:
 */
public interface CustomFilterFunction extends FilterFunction {
    public Tuple2<String, String> tp2(String str);

    public  String getData(String str);

    default boolean remove(Object key, Object value) {
        System.out.println("这是默认实现方法");
        tp2("dt");
        getData("eqt");
        return true;
    }
    public static String comparingByKey() {
       System.out.println("接口静态方法");

        return null;
    }

}
