package com.yankaizhang.springframework.test.config;

import com.yankaizhang.springframework.context.annotation.Bean;
import com.yankaizhang.springframework.context.annotation.Configuration;
import com.yankaizhang.springframework.test.entity.User;

@Configuration
public class MyJavaConfig {

    @Bean
    public User getUser(){
        return new User("dzzhyk", "1123");
    }

}
