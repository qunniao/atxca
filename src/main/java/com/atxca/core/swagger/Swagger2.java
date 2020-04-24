package com.atxca.core.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author 王志鹏
 * @title: Swagger2
 * @projectName baoge
 * @description: TODO
 * @date 2019/3/20 9:30
 */

/**
 * @author Ray
 * @date 2018/7/1 0001
 * Swagger2的配置文件，在项目的启动类的同级文件建立
 */

@Configuration //标记配置类
@EnableSwagger2 //开启在线接口文档
public class Swagger2 {

    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //指向自己的controller即可
                .apis(RequestHandlerSelectors.basePackage("com.atxca"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 设置文档信息
     */
    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                //页面标题
                .title("场地预约 API")
                //描述
                .description("更多资料")
                //版本号
                .version("1.0")
                .build();
    }

}