package com.mybigdata.model;

import lombok.Data;

import java.util.List;

/**
 * @author: 今天风很大
 * @date:2021/7/17 22:44
 * @Description:
 */
@Data
public class StudentAi {
    private Student studentData;//时间戳最大的电池标签
    private List<Student> oldStudentList;
    private String metric;
}
