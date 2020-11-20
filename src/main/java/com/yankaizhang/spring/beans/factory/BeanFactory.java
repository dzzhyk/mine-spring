package com.yankaizhang.spring.beans.factory;

/**
 * 单例工厂的顶层设计
 * @author dzzhyk
 */
public interface BeanFactory {

    /**
     * 根据beanName从IOC中获取实例bean
     */
    Object getBean(String beanName) throws Exception;

    /**
     * 根据beanClass从IOC中获取实例bean
     */
    Object getBean(Class<?> beanClass) throws Exception;

    /**
     * 根据名称和类名获取
     */
    Object getBean(String beanName, Class<?> beanClass) throws Exception;
}
