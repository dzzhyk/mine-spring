package com.yankaizhang.spring.context.support;

import com.yankaizhang.spring.beans.factory.BeanFactory;
import com.yankaizhang.spring.context.config.ConfigurableApplicationContext;

/**
 * 上下文容器实现类的顶层抽象类
 * 实现了通用的公共方法
 * @author dzzhyk
 * @since 2020-11-28 13:51:08
 */
public abstract class AbstractApplicationContext implements ConfigurableApplicationContext {
    /**
     * 刷新容器内容
     */
    public void refresh(){}

    @Override
    public BeanFactory getParentBeanFactory() {
        return null;
    }

    @Override
    public Object getBean(String beanName) throws RuntimeException {
        return null;
    }

    @Override
    public <T> T getBean(Class<T> beanClass) throws RuntimeException {
        return null;
    }

    @Override
    public <T> T getBean(String beanName, Class<T> beanClass) throws RuntimeException {
        return null;
    }
}
