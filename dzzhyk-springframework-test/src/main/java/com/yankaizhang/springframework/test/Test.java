package com.yankaizhang.springframework.test;

import com.yankaizhang.springframework.context.ApplicationContext;
import com.yankaizhang.springframework.test.controller.TestController;

public class Test {
    public static void main(String[] args) {
        ApplicationContext context = new ApplicationContext("application.properties");
        try {
            TestController testController = (TestController) context.getBean("testController");
            testController.hi();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
