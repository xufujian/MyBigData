package com.mybigdata.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: 今天风很大
 * @date:2021/8/23 23:40
 * @Description: 学生分数类
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Score {
    private String name;
    private String course;
    private int score;

    public static Score of(String name,String course,int score){
        return new Score(name,course,score);
    }
}
