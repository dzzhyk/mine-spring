package com.yankaizhang.spring.beans.factory.config;

import com.yankaizhang.spring.beans.BeanDefinitionRegistry;

/**
 * {@link BeanFactoryPostProcessor}的进一步实现接口，优先级高于BeanFactoryPostProcessor<br/>
 * 允许在BeanFactoryPostProcessor执行之前添加新的Bean定义
 * 配置类{@link com.yankaizhang.spring.context.annotation.Configuration}中定义的Bean就是使用这个方式注入的
 * @author dzzhyk
 * @since 2020-12-02 14:45:31
 */
public interface BeanDefinitionRegistryPostProcessor extends BeanFactoryPostProcessor {

    /**
     * 注册某个registry中的bean定义
     * @param registry 某个注册器对象，通常是IoC容器对象
     * @throws Exception 注册过程异常
     */
    void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws Exception;

}
