package com.yankaizhang.spring.beans.factory.config;

import com.yankaizhang.spring.beans.factory.BeanFactory;

/**
 * bean定义的处理器
 * IoC 容器允许 BeanFactoryPostProcessor 在容器实例化任何 bean 之前读取 bean 的定义，并可以修改它
 * @author dzzhyk
 * @since 2020-12-02 14:41:05
 */
public interface BeanFactoryPostProcessor {


    /**
     * 处理IoC容器中的BeanDefinition，此时所有的BeanDefinition都已经被注册，但是还没有实例化
     * @param beanFactory IoC容器
     * @throws Exception 处理过程错误
     */
    void postProcessBeanFactory(BeanFactory beanFactory) throws Exception;

}
