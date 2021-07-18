package com.stu.base.enumstu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @author: 今天风很大
 * @date:2021/7/17 23:28
 * @Description:
 */
@Getter
@AllArgsConstructor
public enum ColorEnum {
    RED("红色", 1), GREEN("绿色", 2), BLANK("白色", 3), YELLO("黄色", 4);
    private String color;
    private int num;

    public static String getName(int index) {
        for (ColorEnum c : ColorEnum.values()) {
            if (c.getNum() == index) {
                return c.color;
            }
        }
        return null;
    }


    public static void main(String[] args) {
        String name = ColorEnum.getName(1);
        System.out.println(name);
    }
}
