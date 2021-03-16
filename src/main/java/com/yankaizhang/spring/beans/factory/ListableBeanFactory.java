package com.yankaizhang.spring.beans.factory;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * ListableBeanFactory 是 BeanFactory接口的进一步具体化接口
 * 该类为bean工厂对象提供了遍历所有bean实例的能力，从而不用每次都进行遍历查询某个bean实例
 * 如果该类的实现同时实现了{@link HierarchicalBeanFactory}接口，那么在遍历的时候不会包括其父类工厂的bean实例对象
 * 如果想要在遍历中包括其父类工厂的实例对象，需要进行额外处理
 * @author dzzhyk
 * @since 2020-12-21 18:06:01
 */
public interface ListableBeanFactory extends BeanFactory {

    /**
     * 判断是否包含某个bean定义
     * @param beanName bean名称
     * @return 判断结果
     */
    boolean containsBeanDefinition(String beanName);

    /**
     * 获取该bean工厂内bean定义的数量
     * @return bean定义的数量
     */
    int getBeanDefinitionCount();

    /**
     * 获取所有bean定义的名字
     * @return bean定义名称列表
     */
    String[] getBeanDefinitionNames();

    /**
     * 获取某个类型的所有bean对象的名称列表
     * @param type bean类型
     * @return bean名称列表
     */
    String[] getBeanNamesForType(Class<?> type);

    /**
     * 获取某类型的所有bean实例对象
     * @param type 某bean类型
     * @param <T> 泛型类T
     * @return 一个Map对象，key是bean名称，value是名称对应的bean实例对象
     * @throws Exception 获取过程异常
     */
    <T> Map<String, T> getBeansOfType(Class<T> type) throws Exception;

    /**
     * 获取拥有某注解的bean实例集合
     * @param annotationType 注解类型
     * @return 一个Map对象，key是bean定义名称，value是名称对应的bean实例对象
     * @throws RuntimeException 获取过程异常
     */
    Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) throws RuntimeException;

    /**
     * 获取某个bean对象上的某类型注解
     * @param beanName bean名称
     * @param annotationType 准备获取的注解类型
     * @param <A> 泛型类A
     * @return 目标注解对象
     * @throws Exception 获取过程异常
     */
    <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType)
            throws Exception;
}
