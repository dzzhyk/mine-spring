package test;

import com.yankaizhang.springframework.context.ApplicationContext;
import test.service.TestService;

public class Test {
    public static void main(String[] args) throws Exception {
        ApplicationContext ac = new ApplicationContext("application.properties");
        TestService testService = (TestService) ac.getBean("testService");
        System.out.println(testService.sayHello("dzzhyk"));
    }
}
