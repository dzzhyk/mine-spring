package com.yankaizhang.springframework.beans.factory.annotation;


import com.yankaizhang.springframework.beans.BeanDefinition;
import com.yankaizhang.springframework.core.type.AnnotationMetadata;
import com.yankaizhang.springframework.core.type.MethodMetadata;


/**
 * 注解配置类型BeanDefinition的通用接口
 */

public interface AnnotatedBeanDefinition extends BeanDefinition {

    /**
     * 注解的元数据
     */
    AnnotationMetadata getMetadata();

    /**
     * 工厂方法的元数据
     */
    MethodMetadata getFactoryMethodMetadata();
}
