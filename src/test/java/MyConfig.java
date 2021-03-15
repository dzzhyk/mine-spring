import com.test.Car;
import com.test.Person;
import com.yankaizhang.spring.beans.factory.annotation.Autowired;
import com.yankaizhang.spring.beans.factory.annotation.Qualifier;
import com.yankaizhang.spring.context.annotation.Bean;
import com.yankaizhang.spring.context.annotation.ComponentScan;
import com.yankaizhang.spring.context.annotation.Configuration;

@Configuration
@ComponentScan("com.test")
public class MyConfig {

//    @Bean(value = "test", initMethod = "initTestPerson", destroyMethod = "destroyTestPerson")
//    public Person testPerson(){
//        return new Person("testPerson", 22, false);
//    }

    @Bean
    public Car car(){
        return new Car("我的otto");
    }
}
