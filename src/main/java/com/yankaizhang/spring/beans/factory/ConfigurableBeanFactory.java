package com.yankaizhang.spring.beans.factory;

import com.yankaizhang.spring.beans.factory.BeanFactory;
import com.yankaizhang.spring.beans.factory.HierarchicalBeanFactory;
import com.yankaizhang.spring.beans.factory.config.BeanPostProcessor;

import java.util.Map;

/**
 * 可以配置的bean工厂接口实现，继承自{@link HierarchicalBeanFactory}接口
 * 实现该接口的bean工厂对象具有可配置的功能，同时提供了一些对于bean实例的进一步管理方法
 * @author dzzhyk
 * @since 2020-12-21 18:30:28
 */
public interface ConfigurableBeanFactory extends HierarchicalBeanFactory {

    /**
     * 设置父工厂
     * @param beanFactory 父工厂实例
     */
    void setParentBeanFactory(BeanFactory beanFactory);

    /**
     * 添加一个BeanPostProcessor对象
     * @param beanPostProcessor bean处理器
     */
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

    /**
     * 获取所有BeanPostProcessor的数量
     * @return BeanPostProcessor的数量
     */
    int getBeanPostProcessorCount();

    /**
     * 删除bean对象
     * @param beanName bean名称
     * @param beanInstance bean实例
     */
    void destroyBean(String beanName, Object beanInstance);

    /**
     * 获取单例容器集合
     * @return singletonIoc
     */
    Map<String, Object> getSingletonIoc();


    /**
     * 清理所有空间
     */
    void destroySingletons();
}
