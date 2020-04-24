package com.atxca.core.filter;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 王志鹏
 * @title: MvcInterceptorConfig
 * @projectName baoge
 * @description: TODO
 * @date 2019/3/26 14:32
 */

@Configuration
public class MvcInterceptorConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则，/**表示拦截所有请求
        // excludePathPatterns 用户排除拦截

        List<String> servicePathPatterns = new ArrayList<String>();
        servicePathPatterns.add("/factoryhouse/information/**");
        servicePathPatterns.add("/factoryhouse/data/**");
        servicePathPatterns.add("/factoryhouse/personal/**");
        servicePathPatterns.add("/information/**");
        servicePathPatterns.add("/data/**");
        servicePathPatterns.add("/personal/**");
        servicePathPatterns.add("/personal/addUser");
        servicePathPatterns.add("/wxlog/**");
        servicePathPatterns.add("/**");


        List<String> excludePathPatterns = new ArrayList<String>();
        excludePathPatterns.add("/personal/addUser");
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns(servicePathPatterns)
                .excludePathPatterns(excludePathPatterns);

        super.addInterceptors(registry);
    }

}
