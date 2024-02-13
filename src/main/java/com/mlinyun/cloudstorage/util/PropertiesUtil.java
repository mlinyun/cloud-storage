package com.mlinyun.cloudstorage.util;

import org.springframework.core.env.Environment;

/**
 * 读取配置文件工具类
 * 该类通过 getProperty 方法获取 application.properties 中的配置
 */
public class PropertiesUtil {

    private static Environment env = null;

    public static void setEnvironment(Environment env) {
        PropertiesUtil.env = env;
    }

    public static String getProperty(String key) {
        return PropertiesUtil.env.getProperty(key);
    }

}
