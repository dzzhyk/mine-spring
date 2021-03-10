package com.yankaizhang.spring.aop.support;

import com.yankaizhang.spring.aop.holder.AopConfig;
import com.yankaizhang.spring.aop.annotation.AfterReturning;
import com.yankaizhang.spring.aop.annotation.AfterThrowing;
import com.yankaizhang.spring.aop.annotation.Before;
import com.yankaizhang.spring.core.LinkedMultiValueMap;
import com.yankaizhang.spring.core.MultiValueMap;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AopAnnotationReader主要完成对AOP切面类的解析
 * @author dzzhyk
 * @since 2021-03-08 19:26:00
 */
public class AopAnnotationReader {

    public AopAnnotationReader() {}

    /**
     * 切点表达式的map
     * key - 切面类对象
     * value - expression表达式
     * TODO: 一个切面类可能定义多个pointcut，目前只能定义一个
     */
    private Map<Class<?>, String> pointCutMap = new HashMap<>();


    public void setPointCut(Class<?> clazz, String execution){
        pointCutMap.put(clazz, execution);
    }

    /**
     * 解析切面类的注解，返回当前切面类的aopConfig包装类
     */
    public List<AdvisedSupport> parseAspect(){

        if (pointCutMap.isEmpty()) {
            return null;
        }

        List<AdvisedSupport> advisedSupports = new ArrayList<>(pointCutMap.size());

        for (Map.Entry<Class<?>, String> entry : pointCutMap.entrySet()) {
            // 当前切面类
            Class<?> aspectClass = entry.getKey();
            // 当前切点表达式
            String pointCutNow = entry.getValue();

            // aopConfig是一个包装类，包含了当前切面的信息
            AopConfig aopConfig = new AopConfig();
            aopConfig.setPointCut(pointCutNow);
            aopConfig.setAspectClass(aspectClass.getTypeName());
            Method[] aspectMethods = aspectClass.getDeclaredMethods();

            // 处理当前切面类的其他注解
            for (Method method : aspectMethods) {
                adviceTypes adviceType = getAdvice(method);
                if (adviceType == null) {
                    continue;
                }
                switch (adviceType){
                    case BEFORE:
                        aopConfig.setAspectBefore(method.getName());break;
                    case AFTER_RETURN:
                        aopConfig.setAspectAfter(method.getName());break;
                    case AFTER_THROW:
                        String exception = method.getAnnotation(AfterThrowing.class).exception();
                        aopConfig.setAspectAfterThrow(method.getName());
                        aopConfig.setAspectAfterThrowingName(exception);
                        break;
                    default:
                        break;
                }
            }

            advisedSupports.add(new AdvisedSupport(aopConfig));
        }

        // 返回所有切面类的包装类集合
        return advisedSupports;
    }

    private adviceTypes getAdvice(Method method){
        for (Annotation annotation : method.getAnnotations()) {
            if (annotation instanceof Before){
                return adviceTypes.BEFORE;
            }else if (annotation instanceof AfterReturning){
                return adviceTypes.AFTER_RETURN;
            }else if (annotation instanceof AfterThrowing){
                return adviceTypes.AFTER_THROW;
            }
        }
        return null;
    }

    /**
     * 通知类型
     */
    private enum adviceTypes{
        // 前置通知
        BEFORE,
        // 返回通知
        AFTER_RETURN,
        // 异常通知
        AFTER_THROW
    }
}
