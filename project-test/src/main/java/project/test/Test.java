package project.test;

import com.yankaizhang.springframework.context.AnnotationConfigApplicationContext;
import project.test.config.MyConfig;
import project.test.entity.Car;
import project.test.entity.User;

public class Test {

    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext( MyConfig.class );
        User user = (User) context.getBean("user");
        Car car = (Car) context.getBean(Car.class);
        Car failedCar = (Car) context.getBean("user", Car.class);

        System.out.println(user);
        System.out.println(car);
        System.out.println(failedCar);
    }

}
