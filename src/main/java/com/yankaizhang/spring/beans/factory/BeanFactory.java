package com.yankaizhang.spring.beans.factory;

/**
 * Bean工厂的顶层设计
 * @author dzzhyk
 * @since 2020-11-28 13:52:36
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

