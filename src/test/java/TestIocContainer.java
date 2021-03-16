import com.test.MyComponent;
import com.test.Person;
import com.yankaizhang.spring.context.impl.AnnotationConfigApplicationContext;

public class TestIocContainer {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MyConfig.class);

        MyComponent myComponent = context.getBean("myComponent", MyComponent.class);
        System.out.println(myComponent);
        context.close();
    }
}
