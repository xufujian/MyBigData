package com.mybigdata.url;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.setting.Setting;

import java.io.File;
import java.net.URL;

/**
 * @author: 今天风很大
 * @date:2021/7/11 13:50
 * @Description:
 */
public class ResourceUrlUtils {

    /**
     * 获取当前模块的配置文件
     * @param clazz
     * @param configName
     * @return
     */
    public static  String getCurrentModuleConfigUrl(Class clazz, String configName) {
        URL resource = clazz.getClassLoader().getResource(configName);
        return resource.getPath();
    }


    public static void main(String[] args) {
        String fileUrl = "/D:/own_project/MyBigdata/flink/target/classes/config.properties";
        Setting setting = new Setting(new File(fileUrl), CharsetUtil.CHARSET_UTF_8, true);//fileUrl是对应文件的具体地址，如E:/code/f.setting
        String uripush = setting.getStr("sendtype");//获取配置文件中的具体参数内容
        String json111 = setting.getStr("input");
        System.out.println(setting.getGroupedMap());
        System.out.println(setting.get("bootstrap.servers"));
    }
}
