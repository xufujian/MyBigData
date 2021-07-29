package com.operator.stu.base.map;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author: 今天风很大
 * @date:2021/7/11 15:09
 * @Description: start-end for:329
 * start -end iter:214
 * 结论，使用iter效率更高，正常能快1倍
 */
public class JavaMap {
    /*
     *使用for循环遍历
     */
    public static void testHashMapByFor(Map<String, Integer> map) {
        for (String key : map.keySet()) {
            Integer value = map.get(key);
//            System.out.println("key:" + key + "->value:" + value);
        }
    }

    /**
     * 使用entry
     *
     * @param map
     */
    public static void testHashMapByIter(Map<String, Integer> map) {
        Iterator<Map.Entry<String, Integer>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, Integer> next = iter.next();
//            System.out.println("key:" + next.getKey() + "->value" + next.getValue());
        }
    }

    public static void main(String[] args) {
        HashMap<String, Integer> map = new HashMap<>();
        int i = 0;
        while (i < 10000000) {
            i++;
            map.put("xu" + i, i);
        }
        long start = System.currentTimeMillis();
        testHashMapByFor(map);
        long end = System.currentTimeMillis();
        System.out.println("start-end for:" + (end - start));
        long start2 = System.currentTimeMillis();
        testHashMapByIter(map);
        long end2 = System.currentTimeMillis();
        System.out.println("start -end iter:" + (end2 - start2));

    }

}
