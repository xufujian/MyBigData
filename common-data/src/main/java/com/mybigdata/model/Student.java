package com.mybigdata.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: 今天风很大
 * @date:2021/7/17 22:40
 * @Description:
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Student implements Serializable {
    private String username;
    private int age;
    private String addr;
    private String recordTime;
}
