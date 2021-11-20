package com.yankaizhang.spring.example;

import com.yankaizhang.spring.context.impl.AnnotationConfigApplicationContext;
import com.yankaizhang.spring.example.config.TestConfig;

/**
 * @author dzzhyk
 * @since 2021-08-25 12:05:32
 * 创建简单容器，注册bean对象然后启动容器
 */
public class BeanLifeExample {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class);
        Object myCar = context.getBean("myAtto");
        context.close();
    }

}
