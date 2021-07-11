package com.stu.base.set;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author: 今天风很大
 * @date:2021/7/11 15:43
 * @Description:
 */
public class JavaSet {

    public static void main(String[] args) {
        Set<Object> set = new HashSet<>();
        Iterator<Object> iter = set.iterator();
        while (iter.hasNext()) {
            Object next = iter.next();
            System.out.println("set：" + next);
        }
        for (Object str : set) {
            System.out.println("str：" + str);
        }
    }
}
