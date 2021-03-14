package com.yankaizhang.spring.beans.factory;

import com.yankaizhang.spring.beans.BeanDefinition;
import com.yankaizhang.spring.beans.factory.annotation.Autowired;
import com.yankaizhang.spring.beans.holder.BeanWrapper;

/**
 * 提供自动装配功能的接口，实现了该接口的bean工厂对象可以对实例进行属性注入等自动装配操作
 * @author dzzhyk
 * @since 2020-12-21 18:24:56
 */
public interface AutowiredBeanFactory extends BeanFactory {

    int AUTOWIRE_NO = 1;
    int AUTOWIRE_BY_NAME = 2;
    int AUTOWIRE_BY_TYPE = 3;
    int AUTOWIRE_CONSTRUCTOR = 4;


    /**
     * 装配使用{@link Autowired}标记的bean对象属性
     */
    void applyAutowiredBeanProperties(Object bean) throws Exception;


    /**
     * 装配用户自定义的，位于bean定义中的已知bean对象属性
     */
    void applyBeanPropertyValues(Object bean, String beanName) throws Exception;


    /**
     * 初始化bean实例，得到bean对象，会调用所有可能的前后置处理器
     * 不会检查是否存在一个bean名称为beanName的bean定义，这个名字只是被当做参数传递给前后置处理器的
     * @return 初始化好的bean对象
     */
    Object initializeBean(Object existingBean, String beanName, BeanDefinition beanDefinition) throws RuntimeException;


    /**
     * 对某个尚未初始化的bean实例，执行前置处理
     * @return {@link BeanWrapper}包装类对象
     */
    Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws RuntimeException;


    /**
     * 对某个初始化完成的bean对象，执行后置处理
     */
    Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws RuntimeException;

}
