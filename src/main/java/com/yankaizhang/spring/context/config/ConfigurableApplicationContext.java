package com.yankaizhang.spring.context.config;

import com.yankaizhang.spring.beans.factory.CompletedBeanFactory;
import com.yankaizhang.spring.context.ApplicationContext;

import java.io.Closeable;

/**
 * 可配置的上下文对象接口，实现了{@link ApplicationContext}上下文核心接口，进一步扩充了上下文容器的配置功能
 * @author dzzhyk
 * @since 2020-12-20 18:04:23
 */
public interface ConfigurableApplicationContext extends ApplicationContext, Closeable {

    /**
     * 获取上下文对象包含的bean工厂
     * @return 返回{@link CompletedBeanFactory}接口的实现类，不返回抽象类的意义在于保证可拓展性
     * @throws IllegalStateException 运行时异常
     */
    CompletedBeanFactory getBeanFactory() throws IllegalStateException;

    /**
     * 刷新或初始化容器
     */
    void refresh();
}
