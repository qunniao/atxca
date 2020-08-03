package com.atxca.Util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogUtil {
    public static void d(String name,String str){
        log.error("******"+name+":"+str+"******");
    }
    public static void i(String str){
        log.error("******"+str+"******");
    }
}
