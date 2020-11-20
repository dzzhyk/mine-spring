package project.test.config;

import com.yankaizhang.springframework.context.annotation.Bean;
import com.yankaizhang.springframework.context.annotation.Configuration;
import com.yankaizhang.springframework.webmvc.ViewResolver;
import project.test.entity.Car;
import project.test.entity.User;

/**
 * @author dzzhyk
 */
@Configuration
public class MyConfig {

    @Bean
    public User user(){
        return new User("dzzhyk", "123");
    }

    @Bean
    public Car car(){
        return new Car("auto", 100000);
    }

    @Bean
    public ViewResolver internalResourceViewResolver(){
        return new ViewResolver("/templates", ".html");
    }

}
