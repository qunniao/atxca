package com.atxca.Util;


import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * @author 王志鹏
 * @title: ReadProperties
 * @projectName baoge
 * @description: TODO
 * @date 2019/3/20 14:33
 */
public class ReadProperties {
    public static void main(String[] args) throws Exception {
        String aa = ReadProperties.getProperties_1("config/config.properties", "serverId");
        System.out.println(aa);
    }

    public static String getProperties_1(String filePath, String keyWord) {
        if (filePath.equals("")) {
            filePath = "config/config.properties";
        }
        Properties prop = null;
        String value = null;
        try {
            // 通过Spring中的PropertiesLoaderUtils工具类进行获取

            prop = PropertiesLoaderUtils.loadAllProperties(filePath);
            // 根据关键字查询相应的值
            value = prop.getProperty(keyWord);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

}
