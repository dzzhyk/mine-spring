package com.yankaizhang.springframework.aop.support;

import com.yankaizhang.springframework.aop.AopConfig;
import com.yankaizhang.springframework.aop.aspect.AfterReturningAdvice;
import com.yankaizhang.springframework.aop.aspect.AfterThrowingAdvice;
import com.yankaizhang.springframework.aop.aspect.MethodBeforeAdvice;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 解析配置文件
 * 封装AopConfig
 */
public class AdvisedSupport {

    private Class<?> targetClass;
    private Object target;
    private Pattern pointCutClassPattern;
    private transient Map<Method, List<Object>> methodCache;    // 存放方法和该方法对应的切面类列表，缓存形式

    private AopConfig aopConfig;

    public AdvisedSupport(AopConfig aopConfig) {
        this.aopConfig = aopConfig;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
        parse();    // 设置了目标代理类之后需要执行解析切点表达式操作
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }


    /**
     * 判断切点是否命中
     */
    public boolean pointCutMatch(){
        return pointCutClassPattern.matcher(this.targetClass.toString()).matches();
    }

    /**
     * 缓存
     */
    public List<Object> getInterceptorsAdvice(Method method, Class<?> targetClass) throws Exception{
        List<Object> cached = methodCache.get(method);
        if (cached == null){
            Method mm = targetClass.getMethod(method.getName(), method.getParameterTypes());
            cached = methodCache.get(mm);
            this.methodCache.put(mm, cached);
        }
        return cached;
    }

    /**
     * 解析切点表达式
     */
    private void parse(){

        // 解析切点表达式操作
        String pointCut = aopConfig.getPointCut()
                .replaceAll("\\.", "\\\\.")
                .replaceAll("\\\\.\\*", ".*")
                .replaceAll("\\(", "\\\\(")
                .replaceAll("\\)", "\\\\)");

        String pointCutForClass = pointCut.substring(0, pointCut.lastIndexOf("\\(") - 4);
        pointCutClassPattern = Pattern.compile("class " + pointCutForClass.substring(pointCutForClass.lastIndexOf(" ") + 1));
        methodCache = new HashMap<>();
        Pattern pattern = Pattern.compile(pointCut);

        try {
            Class<?> aspectClazz = Class.forName(aopConfig.getAspectClass());

            HashMap<String, Method> aspectMethods = new HashMap<>();
            for (Method method : aspectClazz.getMethods()) {
                aspectMethods.put(method.getName(), method);
            }

            for (Method method : targetClass.getMethods()) {
                String methodString = method.toString();
                if (methodString.contains("throws")){
                    methodString = methodString.substring(0, methodString.lastIndexOf("throws")).trim();
                }
                Matcher matcher = pattern.matcher(methodString);
                if (matcher.matches()){
                    // 可以满足切面规则的类就加入到AOP配置中
                    LinkedList<Object> advices = new LinkedList<>();
                    // 前置通知
                    if (!(null == aopConfig.getAspectBefore() || "".equals(aopConfig.getAspectBefore().trim()))){
                        advices.add(new MethodBeforeAdvice(aspectMethods.get(aopConfig.getAspectBefore()), aspectClazz.newInstance()));
                    }
                    // 后置通知
                    if (!(null == aopConfig.getAspectAfter() || "".equals(aopConfig.getAspectAfter().trim()))){
                        advices.add(new AfterReturningAdvice(aspectMethods.get(aopConfig.getAspectAfter()), aspectClazz.newInstance()));
                    }
                    // 异常通知
                    if (!(null == aopConfig.getAspectAfterThrow() || "".equals(aopConfig.getAspectAfterThrow().trim()))){
                        AfterThrowingAdvice afterThrowingAdvice = new AfterThrowingAdvice(aspectMethods.get(aopConfig.getAspectAfterThrow()), aspectClazz.newInstance());
                        afterThrowingAdvice.setThrowingName(aopConfig.getAspectAfterThrowingName());
                        advices.add(afterThrowingAdvice);
                    }
                    methodCache.put(method, advices);
                }
            }

        }catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

    }
}
