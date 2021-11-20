package com.yankaizhang.spring.context.impl;

import com.yankaizhang.spring.context.AnnotationConfigRegistry;
import com.yankaizhang.spring.context.config.AnnotatedBeanDefinitionReader;
import com.yankaizhang.spring.context.config.ClassPathBeanDefinitionScanner;
import com.yankaizhang.spring.context.generic.GenericApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 真正使用到的IOC容器，直接接触用户<br/>s
 * 继承自{@link GenericApplicationContext}，并且实现了refresh方法<br/>
 * AnnotationConfigApplicationContext是专门用来解析注解配置类的容器对象<br/>
 * 实现了{@link AnnotationConfigRegistry}接口，说明拥有基本的两个方法scan和register<br/>
 * @author dzzhyk
 * @since 2021-03-08 10:38:18
 */
@SuppressWarnings("all")
public class AnnotationConfigApplicationContext extends GenericApplicationContext implements AnnotationConfigRegistry {

    private static final Logger log = LoggerFactory.getLogger(AnnotationConfigApplicationContext.class);

    private AnnotatedBeanDefinitionReader reader;
    private ClassPathBeanDefinitionScanner scanner;


    private AnnotationConfigApplicationContext() {
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