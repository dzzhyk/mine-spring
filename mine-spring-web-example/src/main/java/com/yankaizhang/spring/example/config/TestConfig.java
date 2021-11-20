package com.yankaizhang.spring.example.config;

import com.yankaizhang.spring.context.annotation.Bean;
import com.yankaizhang.spring.context.annotation.Configuration;
import com.yankaizhang.spring.context.annotation.EnableAspectJAutoProxy;
import com.yankaizhang.spring.example.entity.User;
import com.yankaizhang.spring.web.view.HtmlView;
import com.yankaizhang.spring.web.ViewResolver;
import com.yankaizhang.spring.web.view.JspView;
import com.yankaizhang.spring.webmvc.multipart.commons.CommonsMultipartResolver;

/**
 * 示例配置类
 * @author dzzhyk
 */
@Configuration
@EnableAspectJAutoProxy
public class TestConfig {

    @Bean
    public ViewResolver internalResourceViewResolver(){
        ViewResolver viewResolver = new ViewResolver();
        viewResolver.setPrefix("/WEB-INF/");
        viewResolver.setSuffix(".jsp");
        viewResolver.setContentType("text/html;charset=UTF-8");
        viewResolver.setViewClass(JspView.class);
        return viewResolver;
    }


    /**
     * multipart文件处理器
     */
    @Bean
    public CommonsMultipartResolver multipartResolver(){
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        // 200MB
        multipartResolver.setMaxUploadSize(1024 * 1024 * 200);
        multipartResolver.setDefaultEncoding("utf-8");
        multipartResolver.setMaxUploadSizePerFile(1024 * 1024 * 20);
        multipartResolver.setMaxInMemorySize(1024 * 10);

        return multipartResolver;
    }


    @Bean
    public User user(){
        return new User("cool", "123123");
    }
}
