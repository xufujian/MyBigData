package com.kafka.utils;

import com.mybigdata.common.CommonUtils;
import com.mybigdata.model.Student;
import com.mybigdata.time.TimeUtils;

/**
 * @author: 今天风很大
 * @date:2021/7/18 13:54
 * @Description:
 */
public class KafkaData {
    public static Student producerDataOfStudent() {
        String randomAreaCity = CommonUtils.getRandomAreaCity();
        String currentDate = TimeUtils.getCurrentDate(System.currentTimeMillis());
        int randomAge = CommonUtils.getRandomAge(100);
        String username = CommonUtils.getRandomStr(5);
        return new Student(username, randomAge, randomAreaCity, currentDate);
    }
}
