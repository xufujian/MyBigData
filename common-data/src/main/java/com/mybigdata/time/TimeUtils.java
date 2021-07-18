package com.mybigdata.time;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

/**
 * @author: 今天风很大
 * @date:2021/7/18 13:57
 * @Description: 时间工具类
 */
public class TimeUtils {

    /**
     * 精确到毫秒
     * @param date
     * @return
     */
    public static String getCurrentDate(long date){
        return DateUtil.date(date).toString("yyyy-MM-dd HH:mm:ss:SSS");
    }

    public static void main(String[] args) {
       System.out.println(getCurrentDate(System.currentTimeMillis()));
    }
}
