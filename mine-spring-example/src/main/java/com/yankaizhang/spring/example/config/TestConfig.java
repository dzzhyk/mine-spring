package com.yankaizhang.spring.example.config;

import com.yankaizhang.spring.context.annotation.Bean;
import com.yankaizhang.spring.context.annotation.Configuration;
import com.yankaizhang.spring.web.ViewResolver;

/**
 * @author dzzhyk
 */
@Configuration
public class TestConfig {

    @Bean
    public ViewResolver internalResourceViewResolver(){
        ViewResolver viewResolver = new ViewResolver();
        viewResolver.setPrefix("templates");
        viewResolver.setSuffix(".html");
        viewResolver.setContentType("text/html;charset=utf-8");
        return viewResolver;
    }

}
