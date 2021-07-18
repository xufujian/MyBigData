package com.mybigdata.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: 今天风很大
 * @date:2021/7/18 14:24
 * @Description:
 */
public class GsonUtil {
    private static Logger logger = LoggerFactory.getLogger(GsonUtil.class);

    private static Gson filterNullGson;
    private static Gson nullableGson;

    static {
        //过滤null
        filterNullGson = new GsonBuilder()
                .enableComplexMapKeySerialization()
                .setDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
                .create();
        //null 序列化
        nullableGson = new GsonBuilder()
                .enableComplexMapKeySerialization()
                .serializeNulls()
                .setDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
                .create();
    }

    public static String toJsonHoldNullField(Object obj) {
        return nullableGson.toJson(obj);
    }

    public static String toJsonFilterNullField(Object obj) {
        return filterNullGson.toJson(obj);
    }

    public static <T> T getObject(String raw, Class<T> clazz) {
        T t = null;
        try {
            t = filterNullGson.fromJson(raw, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    public static void main(String[] args) {
    }
}