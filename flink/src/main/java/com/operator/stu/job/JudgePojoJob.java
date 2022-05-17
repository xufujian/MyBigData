package com.operator.stu.job;

import com.mybigdata.model.Score;
import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.typeinfo.TypeInformation;

/**
 * @author: 今天风很大
 * @date:2021/8/25 23:28
 * @Description: 判断一个对象是否是 java pojo
 */
public class JudgePojoJob {

    public static void main(String[] args) {
        System.out.println(TypeInformation.of(Score.class).createSerializer(new ExecutionConfig()));
    }
}
