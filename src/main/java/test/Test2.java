package test;

import com.yankaizhang.springframework.aop.support.ClazzUtils;
import test.service.TestService;
import test.service.impl.TestServiceImpl;

import java.util.List;
import java.util.regex.Pattern;

public class Test2 {
    public static void main(String[] args) {

        String pointCut = "* test.service.impl.TestServiceImpl.*(*)";

        // test\.service\..*\..*\(.*\)
        // test\.service\..*\..*\(\..*\)

        // 解析切点表达式操作
        pointCut = pointCut
                .replaceAll("\\.", "\\\\.")
                .replaceAll("\\\\.\\*", "\\\\..*")
                .replaceAll("\\(", "\\\\(")
                .replaceAll("\\)", "\\\\)");

        System.out.println(pointCut);

        String pointCutForClass = pointCut.substring(0, pointCut.lastIndexOf("\\(") - 4);
        // 没处理public abstract void值
        // 只处理了类名
        Pattern pointCutClassPattern = Pattern.compile("class " + pointCutForClass.substring(pointCutForClass.lastIndexOf(" ") + 1));
        System.out.println("[pointCut] " + pointCut);
        System.out.println("[pointCutForClass] " + pointCutForClass);
        System.out.println("[pointCutClassPattern] " + pointCutClassPattern);

        TestServiceImpl testService = new TestServiceImpl();
        TestService testService1 = new TestServiceImpl();
        System.out.println("[testService.class] " + testService.getClass());
        System.out.println("[testService1.class] " + testService1.getClass());

        System.out.println("[isMatched] " + pointCutMatch(pointCutClassPattern, testService.getClass()));
        System.out.println("[isMatched] " + pointCutMatch(pointCutClassPattern, testService1.getClass()));

        String packageName = pointCutForClass.substring(pointCutForClass.lastIndexOf(" ") + 1).replaceAll("\\\\.", ".");
        List<String> clazzName = ClazzUtils.getClazzName(packageName, true);

        System.out.println(clazzName);
    }

    /**
     * 判断切点是否命中
     */
    private static boolean pointCutMatch(Pattern pattern, Class<?> clazz){
        return pattern.matcher(clazz.toString()).matches();
    }
}
