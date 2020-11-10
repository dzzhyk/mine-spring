package com.yankaizhang.springframework.context.config;

import com.yankaizhang.springframework.aop.support.AopUtils;
import com.yankaizhang.springframework.beans.BeanDefinition;
import com.yankaizhang.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import com.yankaizhang.springframework.beans.factory.support.RootBeanDefinition;
import com.yankaizhang.springframework.context.BeanDefinitionRegistry;
import com.yankaizhang.springframework.context.annotation.Component;
import com.yankaizhang.springframework.context.util.BeanDefinitionRegistryUtils;
import com.yankaizhang.springframework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * AnnotatedBeanDefinitionReader主要完成对注解配置类的解析
 *
 * 这里是源码的一个简化实现
 */
public class AnnotatedBeanDefinitionReader {

    public static final Logger log = LoggerFactory.getLogger(AnnotatedBeanDefinitionReader.class);
    private BeanDefinitionRegistry registry;

    /**
     * 通过构造方法获取从ApplicationContext传入的locations路径，然后解析扫描和保存相关所有的类并且提供统一的访问入口
     */
    public AnnotatedBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this.registry = registry;
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
