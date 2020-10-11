package test;

import com.yankaizhang.springframework.aop.PointCutConfig;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test3 {
    public static void main(String[] args) {

        try {
            Class<?> clazz = Class.forName("test.aspect.LogAspect");

            for (Method method : clazz.getDeclaredMethods()) {
                if (method.getName().equals("before")){
                    PointCutConfig pointCutConfig = new PointCutConfig(method);
                    System.out.println(pointCutConfig.toString());
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
