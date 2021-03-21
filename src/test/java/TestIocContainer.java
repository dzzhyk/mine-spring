import com.component.CompA;
import com.component.CompB;
import com.component.CompC;
import com.component.CompD;
import com.test.MyComponent;
import com.test.Person;
import com.yankaizhang.spring.context.impl.AnnotationConfigApplicationContext;

public class TestIocContainer {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MyConfig.class);
//        CompC compC = context.getBean("compC", CompC.class);
//        compC.hello();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                CompD td = context.getBean("compD", CompD.class);
                System.out.println(Thread.currentThread().getName() + " : " + td);
            }
        });

        t.start();

        CompD compD = context.getBean("compD", CompD.class);
        System.out.println(Thread.currentThread().getName() + " : " + compD);

        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        context.close();
    }
}
