package com.yankaizhang.spring.context.config;

import com.yankaizhang.spring.beans.factory.impl.AnnotatedGenericBeanDefinition;
import com.yankaizhang.spring.beans.BeanDefinitionRegistry;
import com.yankaizhang.spring.context.util.BeanDefinitionRegistryUtils;
import com.yankaizhang.spring.util.StringUtils;

/**
 * AnnotatedBeanDefinitionReader主要完成对注解配置类的解析
 * 这里是源码的一个简化实现
 * @author dzzhyk
 * @since 2020-11-28 13:50:48
 */
public class AnnotatedBeanDefinitionReader {

    private BeanDefinitionRegistry registry;
    private final String CONFIGURATION_CLASS_POST_PROCESSOR = "internalConfigurationClassPostProcessor";

    /**
     * 通过构造方法获取从ApplicationContext传入的locations路径，然后解析扫描和保存相关所有的类并且提供统一的访问入口
     */
    public AnnotatedBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this.registry = registry;
        // TODO: 在这里将ConfigurationClassPostProcessor的bean定义加入
        registerBean(ConfigurationClassPostProcessor.class, CONFIGURATION_CLASS_POST_PROCESSOR);
    }

    /**
     * 注册bean定义
     */
    public void register(Class<?>... componentClasses) {
        for (Class<?> componentClass : componentClasses) {
            registerBean(componentClass);
        }
    }

    public void registerBean(Class<?> componentClass) {
        doRegisterBean(componentClass, null);
    }

    public void registerBean(Class<?> componentClass, String name) {
        doRegisterBean(componentClass, name);
    }


    /**
     * 给定beanClass，收集他的注解信息，然后注册一个BeanDefinition
     */
    private <T> void doRegisterBean(Class<?> beanClass, String name){

        AnnotatedGenericBeanDefinition beanDef = new AnnotatedGenericBeanDefinition(beanClass);
        String beanName = (name==null? StringUtils.toLowerCase(beanClass.getSimpleName()) : name);

        // TODO: 收集类的注册信息

        BeanDefinitionRegistryUtils.registerBeanDefinition(registry, beanName, beanDef);
    }

    public BeanDefinitionRegistry getRegistry() {
        return registry;
    }
}
