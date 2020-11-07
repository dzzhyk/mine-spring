package com.yankaizhang.springframework.test;


import com.yankaizhang.springframework.context.AnnotationConfigApplicationContext;
import com.yankaizhang.springframework.test.config.MyJavaConfig;
import com.yankaizhang.springframework.test.controller.TestController;
import org.junit.Test;

public class TestAop {
    @Test
    public void testAop(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext( MyJavaConfig.class );
        try {
            TestController testController = (TestController) context.getBean("testController");
            testController.hi();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
