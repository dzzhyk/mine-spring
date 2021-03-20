import com.component.CompA;
import com.component.CompB;
import com.component.CompC;
import com.test.MyComponent;
import com.test.Person;
import com.yankaizhang.spring.context.impl.AnnotationConfigApplicationContext;

public class TestIocContainer {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MyConfig.class);
        CompC compC = context.getBean("compC", CompC.class);
        compC.hello();
        context.close();
    }
}
