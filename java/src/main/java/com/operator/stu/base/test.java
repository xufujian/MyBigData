package com.operator.stu.base;

import java.util.Optional;

/**
 * @author: 今天风很大
 * @date:2021/8/1 23:02
 * @Description:
 */
public class test {
    public static void main(String[] args) {

        Optional<String> s = Optional.of("3");

        System.out.println(s.get());

        System.out.println(s.isPresent());
    }
}
