package com.yankaizhang.spring.beans.factory.annotation;


import com.yankaizhang.spring.beans.BeanDefinition;
import com.yankaizhang.spring.core.type.AnnotationMetadata;
import com.yankaizhang.spring.core.type.MethodMetadata;


/**
 * 注解配置类型BeanDefinition的通用接口
 * @author dzzhyk
 * @since 2020-11-28 13:51:45
 */
public interface AnnotatedBeanDefinition extends BeanDefinition {

    /**
     * 注解的元数据
     * @return 返回注解元数据
     */
    AnnotationMetadata getMetadata();

    /**
     * 工厂方法的元数据
     * @return 返回工厂方法元数据
     */
    MethodMetadata getFactoryMethodMetadata();
}
