package com.yankaizhang.spring.beans.factory;

/**
 * Bean工厂的顶层设计
 * @author dzzhyk
 * @since 2020-11-28 13:52:36
 */
public interface BeanFactory {

    /**
     * 根据beanName从IOC中获取实例bean
     * @param beanName bean名称
     * @return bean实例对象
     * @throws RuntimeException 异常
     */
    Object getBean(String beanName) throws RuntimeException;

    /**
     * 根据beanClass从IOC中获取实例bean
     * @param beanClass bean类型
     * @param <T> 某类型
     * @return 某类型bean实例对象
     * @throws RuntimeException 异常
     */
    <T> T getBean(Class<T> beanClass) throws RuntimeException;

    /**
     * 根据名称和类名获取
     * @param beanName bean名称
     * @param beanClass bean类型
     * @param <T> 某类型
     * @return 某类型bean实例对象
     * @throws RuntimeException 异常
     */
    <T> T getBean(String beanName, Class<T> beanClass) throws RuntimeException;

    /**
     * 判断容器中是否包含某个bean对象
     * @param beanName beanName
     * @return 判断结果
     */
    boolean containsBean(String beanName);

    /**
     * 判断某个bean对象是否为单例
     * @param beanName bean名称
     * @return 判断结果
     * @throws Exception 判断异常
     */
    boolean isSingleton(String beanName) throws Exception;
}

