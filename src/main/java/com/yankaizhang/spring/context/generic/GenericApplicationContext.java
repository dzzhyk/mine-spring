package com.yankaizhang.spring.context.generic;

import com.yankaizhang.spring.beans.BeanDefinition;
import com.yankaizhang.spring.beans.BeanDefinitionRegistry;
import com.yankaizhang.spring.beans.factory.impl.DefaultBeanFactory;
import com.yankaizhang.spring.context.support.AbstractApplicationContext;
import com.yankaizhang.spring.util.Assert;

/**
 * 通用上下文对象
 * 拥有所有上下文对象的通用操作 - 处理Bean定义
 * @author dzzhyk
 * @since 2020-12-20 18:06:33
 */
public class GenericApplicationContext extends AbstractApplicationContext implements BeanDefinitionRegistry {

    /** 真正的beanFactory对象 */
    DefaultBeanFactory beanFactory;

    public GenericApplicationContext() {
        this.beanFactory = new DefaultBeanFactory();
    }

    public GenericApplicationContext(DefaultBeanFactory beanFactory) {
        Assert.notNull(beanFactory, "用于初始化的beanFactory不能为null");
        this.beanFactory = beanFactory;
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws Exception {
        beanFactory.registerBeanDefinition(beanName, beanDefinition);
    }

    @Override
    public void removeBeanDefinition(String beanName) throws Exception {
        beanFactory.removeBeanDefinition(beanName);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) throws Exception {
        return beanFactory.getBeanDefinition(beanName);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanFactory.containsBeanDefinition(beanName);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return beanFactory.getBeanDefinitionNames();
    }

    @Override
    public int getBeanDefinitionCount() {
        return beanFactory.getBeanDefinitionCount();
    }

    @Override
    public boolean isBeanNameInUse(String beanName) {
        return beanFactory.isBeanNameInUse(beanName);
    }

}
