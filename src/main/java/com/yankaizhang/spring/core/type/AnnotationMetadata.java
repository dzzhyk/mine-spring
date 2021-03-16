package com.yankaizhang.spring.core.type;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * 注解的元数据接口定义，定义了一些访问注解内容的接口方法<br/>
 *<br/>
 * 注解本身也是类实现，所以继承了ClassMetadata<br/>
 * 注解可以有注解，因此继承了AnnotatedTypeMetadata
 * @author dzzhyk
 * @since 2020-11-28 13:46:46
 */
public interface AnnotationMetadata extends ClassMetadata, AnnotatedTypeMetadata {

    /**
     * 获取指定注解标注的所有方法元数据集合
     * @param annotationClass 指定注解类
     * @return 方法元数据集合
     */
    Set<MethodMetadata> getAnnotatedMethods(Class<? extends Annotation> annotationClass);

    /**
     * 判断该方法是否被指定注解标记
     * @param annotationClass 注解类
     * @return 判断结果
     */
    default boolean hasAnnotatedMethods(Class<? extends Annotation> annotationClass){
        return getAnnotatedMethods(annotationClass)!=null;
    }


    /**
     * 使用标准反射为给定类对象创建注解原信息
     * @param beanClass 指定类对象
     * @return 指定类的注解元信息
     */
    static AnnotationMetadata introspect(Class<?> beanClass) {
        return new StandardAnnotationMetadata(beanClass);
    }
}
