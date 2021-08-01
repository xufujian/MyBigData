package com.operator.stu.base.abstractStu;

import java.util.function.BiFunction;

/**
 * @author: 今天风很大
 * @date:2021/7/30 23:40
 * @Description:
 */
public class Test {
    private static void doSomething(MyFunctionInterface myFunctionInterface){
        myFunctionInterface.say();
    }

    public static void main(String[] args) {

        doSomething(()->System.out.println("daf"));


        new Student() {
            @Override
            public int getAge() {
                return super.getAge();
            }

            @Override
            void getName() {
                super.getName();
            }
        };

        BiFunction<String, String, String> apply = new BiFunction<String, String, String>() {
            @Override
            public String apply(String s, String s2) {
                System.out.println("apply");
                return null;
            }

        };
        apply.apply("45","325");



    }
}
