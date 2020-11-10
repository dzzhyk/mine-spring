package com.yankaizhang.springframework.core.type;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * 注解的元数据接口定义，定义了一些访问注解内容的接口方法
 *
 * 注解本身也是类实现，所以继承了ClassMetadata
 * 注解可以有注解，因此继承了AnnotatedTypeMetadata
 */
public interface AnnotationMetadata extends ClassMetadata, AnnotatedTypeMetadata {

    /**
     * 获取该注解标注的所有方法
     */
    Set<MethodMetadata> getAnnotatedMethods(Class<? extends Annotation> annotationClass);

    default boolean hasAnnotatedMethods(Class<? extends Annotation> annotationClass){
        return getAnnotatedMethods(annotationClass)!=null;
    }


    /**
     * 使用标准反射为给定类对象创建注解原信息
     */
    static AnnotationMetadata introspect(Class<?> beanClass) {
        return new StandardAnnotationMetadata(beanClass);
    }
}
