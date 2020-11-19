package com.yankaizhang.springframework.example.config;

import com.yankaizhang.springframework.context.annotation.Bean;
import com.yankaizhang.springframework.context.annotation.ComponentScan;
import com.yankaizhang.springframework.context.annotation.Configuration;
import com.yankaizhang.springframework.example.controller.TestController;
import com.yankaizhang.springframework.example.entity.User;
import com.yankaizhang.springframework.example.service.TestService;
import com.yankaizhang.springframework.example.service.impl.TestServiceImpl;
import com.yankaizhang.springframework.webmvc.multipart.commons.CommonsMultipartResolver;

/**
 * 支持使用@ComponentScan注解扫描额外包
 * @author dzzhyk
 */
@Configuration
@ComponentScan("com.yankaizhang.springframework.another")
public class MyJavaConfig {

    @Bean
    public User user(){
        return new User("dzzhyk", "1123");
    }

    /**
     * 添加文件处理器bean
     */
    @Bean
    public CommonsMultipartResolver multipartResolver(){
        return new CommonsMultipartResolver();
    }

    @Bean
    public TestController testController(){
        return new TestController();
    }

    @Bean
    public TestService testService(){
        return new TestServiceImpl();
    }
}
