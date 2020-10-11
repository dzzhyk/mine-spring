package com.yankaizhang.springframework.aop.support;

import com.yankaizhang.springframework.aop.AopConfig;
import com.yankaizhang.springframework.aop.PointCutConfig;
import com.yankaizhang.springframework.aop.aspect.AfterReturningAdvice;
import com.yankaizhang.springframework.aop.aspect.AfterThrowingAdvice;
import com.yankaizhang.springframework.aop.aspect.MethodBeforeAdvice;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 解析配置文件
 * 封装AopConfig
 */
public class AdvisedSupport implements Cloneable {

    private Class<?> targetClass;
    private Object target;
    private Pattern pointCutClassPattern;
    private transient Map<Method, List<Object>> methodCache;    // 存放方法和该方法对应的切面类列表，缓存形式
    private String preProcessPointCut;

    private AopConfig aopConfig;

    public AdvisedSupport(AopConfig aopConfig) {
        this.aopConfig = aopConfig;
        preParse();
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class<?> targetClass) throws Exception {
        this.targetClass = targetClass;
        if (null != targetClass)
            parseMethod();    // 设置了新的目标代理类之后需要执行解析切入方法操作
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public AopConfig getAopConfig() {
        return aopConfig;
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
     * 预处理切点表达式
     */
    private void preParse(){
        // 解析切点表达式操作
        preProcessPointCut = aopConfig.getPointCut()
                .replaceAll("\\.", "\\\\.")
                .replaceAll("\\\\.\\*", "\\\\..*")
                .replaceAll("\\(", "\\\\(")
                .replaceAll("\\)", "\\\\)");

        String pointCutForClass = preProcessPointCut.substring(0, preProcessPointCut.lastIndexOf("\\(") - 4);
        pointCutClassPattern = Pattern.compile("class " + pointCutForClass.substring(pointCutForClass.lastIndexOf(" ") + 1));
        methodCache = new HashMap<>();
    }

    /**
     * 解析切点表达式方法
     */
    private void parseMethod() throws Exception{

        try {
            Class<?> aspectClazz = Class.forName(aopConfig.getAspectClass());

            HashMap<String, Method> aspectMethods = new HashMap<>();
            for (Method method : aspectClazz.getDeclaredMethods()) {
                aspectMethods.put(method.getName(), method);
            }

            for (Method method : targetClass.getDeclaredMethods()) {    // getDeclaredMethods

                // 创建pointCut包装类
                PointCutConfig pointCutConfig = new PointCutConfig(method);

                if (pointCutConfig.matches(aopConfig.getPointCut())){
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

    /**
     * 根据切点表达式获取路径下的所有在IoC容器中的类名命名
     * 首字母小写
     */
    public List<String> parseClasses() {
        String pointCutForClass = preProcessPointCut.substring(0, preProcessPointCut.lastIndexOf("\\(") - 4);
        String packageName = pointCutForClass.substring(pointCutForClass.lastIndexOf(" ") + 1).replaceAll("\\\\.", ".")
                .replaceAll("\\.+", ".")
                .replaceAll("\\.\\*", "");

        return ClazzUtils.getClazzName(packageName, true);
    }

    @Override
    public AdvisedSupport clone() throws CloneNotSupportedException {
        return  (AdvisedSupport) super.clone();
    }

    public void setPointCutClassPattern(Pattern pointCutClassPattern) {
        this.pointCutClassPattern = pointCutClassPattern;
    }

    public void setMethodCache(Map<Method, List<Object>> methodCache) {
        this.methodCache = methodCache;
    }

    public void setPreProcessPointCut(String preProcessPointCut) {
        this.preProcessPointCut = preProcessPointCut;
    }

    public void setAopConfig(AopConfig aopConfig) {
        this.aopConfig = aopConfig;
    }
}
