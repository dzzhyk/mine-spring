package com.yankaizhang.springframework.beans.factory;

/**
 * 单例工厂的顶层设计
 */
public interface BeanFactory {

    /**
     * 根据beanName从IOC中获取实例bean
     */
    Object getBean(String beanName) throws Exception;

    /**
     * 根据beanClass从IOC中获取实例bean
     */
    public Object getBean(Class<?> beanClass) throws Exception;
}
