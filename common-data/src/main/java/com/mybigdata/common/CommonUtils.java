package com.mybigdata.common;

import java.util.Random;

/**
 * @author: 今天风很大
 * @date:2021/7/18 14:07
 * @Description: 通用的一些方法
 */
public class CommonUtils {
    private static final String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";


    /**
     * 随机获得一个省份
     *
     * @return
     */
    public static String getRandomAreaCity() {
        String citys[] = {"北京", "广东", "山东", "江苏", "河南", "上海", "河北", "浙江", "香港", "山西", "陕西", "湖南", "重庆", "福建", "天津", "云南", "四川", "广西", "安徽", "海南", "江西", "湖北", "山西", "辽宁", "内蒙古"};
        int randomInt = new Random().nextInt(10000) % citys.length;
        return citys[randomInt];
    }

    /**
     * 随机生成数字
     *
     * @param numRange
     * @return
     */
    public static int getRandomAge(int numRange) {
        return new Random().nextInt(numRange);
    }

    public static String getRandomStr(int strLength) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < strLength; i++) {
            sb.append(str.charAt(random.nextInt(str.length())));
        }
        return sb.toString();
    }

    public static void main(String[] args) {

        System.out.println(getRandomStr(5));
    }
}
