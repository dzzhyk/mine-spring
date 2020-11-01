package com.yankaizhang.springframework.test;

import com.yankaizhang.springframework.context.AnnotationConfigApplicationContext;
import com.yankaizhang.springframework.test.controller.TestController;

public class Test {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("application.properties");
        try {
            TestController testController = (TestController) context.getBean("testController");
            testController.hi();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
