package com.operator.stu.base.list;

import com.mybigdata.model.Student;

import java.util.ArrayList;

import java.util.Comparator;
import java.util.Optional;

/**
 * @author: 今天风很大
 * @date:2021/7/17 22:39
 * @Description: 求集合最大值
 */
public class List {
    public static void main(String[] args) {
        ArrayList<Student> arrList = new ArrayList<>();
        arrList.add(new Student("xu1", 24, "江苏", "2021-07-17 19:17:54"));
        arrList.add(new Student("xu1", 45, "湖北", "2021-07-17 22:12:34"));
        arrList.add(new Student("xu1", 67, "浙江", "2021-07-17 20:22:21"));
        arrList.add(new Student("xu1", 89, "云南", "2021-07-17 21:18:56"));

        Optional<Student> max1 = arrList.parallelStream().max(Comparator.comparing(Student::getRecordTime));
        System.out.println(max1);
        Optional<Student> max2 = arrList.parallelStream().max(new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                return o1.getRecordTime().compareTo(o2.getRecordTime());
            }
        });
        System.out.println(max2);
    }
}
