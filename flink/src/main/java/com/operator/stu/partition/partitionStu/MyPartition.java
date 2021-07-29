package com.operator.stu.partition.partitionStu;

import org.apache.flink.api.common.functions.Partitioner;

/**
 * @author: 今天风很大
 * @date:2021/7/19 23:04
 * @Description:
 */
public class MyPartition implements Partitioner<Long> {
    @Override
    public int partition(Long key, int numPartitions) {
        System.out.println("分区总数:" + numPartitions);
        if (key % 2 == 0) {
            return 0;
        } else {
            return 1;
        }
    }
}
