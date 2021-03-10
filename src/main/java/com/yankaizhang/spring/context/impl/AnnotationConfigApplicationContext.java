package com.yankaizhang.spring.context.impl;

import com.yankaizhang.spring.beans.factory.config.BeanFactoryPostProcessor;
import com.yankaizhang.spring.beans.factory.config.BeanPostProcessor;
import com.yankaizhang.spring.context.AnnotationConfigRegistry;
import com.yankaizhang.spring.context.config.AnnotatedBeanDefinitionReader;
import com.yankaizhang.spring.context.config.ClassPathBeanDefinitionScanner;
import com.yankaizhang.spring.context.generic.GenericApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * 真正使用到的IOC容器，直接接触用户
 * 继承了DefaultListableBeanFactory，并且把里面的refresh方法实现了
 * 实现了BeanFactory接口，实现了getBean()方法
 * 完成IoC、DI、AOP的衔接
 * AnnotationConfigApplicationContext是专门用来解析注解配置类的容器对象
 *
 * 实现了AnnotationConfigRegistry接口，说明拥有基本的两个方法scan和register
 *
 * TODO: 这里其实对于AnnotationConfigApplicationContext而言，在这一层实现了多个功能，其实是简化过后的
 * @author dzzhyk
 * @since 2021-03-08 10:38:18
 */
@SuppressWarnings("all")
public class AnnotationConfigApplicationContext extends GenericApplicationContext implements AnnotationConfigRegistry {

    private static final Logger log = LoggerFactory.getLogger(AnnotationConfigApplicationContext.class);

    private AnnotatedBeanDefinitionReader reader;
    private ClassPathBeanDefinitionScanner scanner;


    public AnnotationConfigApplicationContext() {
        this.reader = new AnnotatedBeanDefinitionReader(this);
        this.scanner = new ClassPathBeanDefinitionScanner(this);
    }

    /**
     * 从其他容器创建容器
     *
     * @param context
     */
    public AnnotationConfigApplicationContext(AnnotationConfigApplicationContext context) {
        this();
        this.reader = context.reader;
        this.scanner = context.scanner;
    }

    /**
     * 传入配置类初始化容器
     */
    public AnnotationConfigApplicationContext(Class<?>... configClass) {
        this();
        register(configClass);
        refresh();
    }

    /**
     * 传入待扫描路径初始化容器
     */
    public AnnotationConfigApplicationContext(String... basePackages) {
        this();
        scan(basePackages);
        refresh();
    }


    @Override
    public void register(Class<?>... componentClasses) {
        this.reader.register(componentClasses);
    }

    @Override
    public void scan(String... basePackages) {
        this.scanner.scan(basePackages);
    }

}