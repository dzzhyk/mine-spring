package com.yankaizhang.spring.beans.factory;

import com.yankaizhang.spring.beans.BeanDefinition;
import com.yankaizhang.spring.beans.factory.support.AbstractCompletedBeanFactory;

/**
 * 实现了三种功能接口的综合接口
 * Completed意味着"完全的"，包括了配置、遍历、自动装配全部三个功能
 * 作者认为，该接口存在主要是保证了可扩展性，不然直接返回{@link AbstractCompletedBeanFactory}抽象类的实现类效果相同
 * 一般不直接使用该接口，而是将使用该接口类接收返回值，以便提供完全的beanFactory操作能力
 * @author dzzhyk
 * @since 2020-12-21 20:55:38
 */
public interface CompletedBeanFactory extends ListableBeanFactory, ConfigurableBeanFactory, AutowiredBeanFactory {

    /**
     * 通过beanName获取BeanDefinition
     * @param beanName bean名称
     * @return beanName对应的bean定义
     * @throws Exception 处理异常
     */
    BeanDefinition getBeanDefinition(String beanName) throws Exception;

    /**
     * 预先实例化需要的单例，一般在BeanFactory初始化的最后一步进行
     * @throws Exception 实例化过程异常
     */
    void preInstantiateSingletons() throws Exception;

}
