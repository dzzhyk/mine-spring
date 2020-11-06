package com.yankaizhang.springframework.test.config;

import com.yankaizhang.springframework.context.annotation.Bean;
import com.yankaizhang.springframework.context.annotation.Configuration;
import com.yankaizhang.springframework.test.entity.User;
import com.yankaizhang.springframework.webmvc.multipart.commons.CommonsMultipartResolver;

@Configuration
public class MyJavaConfig {

    @Bean
    public User getUser(){
        return new User("dzzhyk", "1123");
    }

    /**
     * 添加文件处理器bean
     */
    @Bean
    public CommonsMultipartResolver multipartResolver(){
        return new CommonsMultipartResolver();
    }
}
