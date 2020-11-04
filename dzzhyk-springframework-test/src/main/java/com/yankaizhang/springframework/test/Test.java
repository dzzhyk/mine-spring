package com.yankaizhang.springframework.test;

import com.yankaizhang.springframework.context.AnnotationConfigApplicationContext;
import com.yankaizhang.springframework.test.entity.User;

public class Test {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("application.properties");
        try {
            User user = (User) context.getBean("getUser");
            System.out.println(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
