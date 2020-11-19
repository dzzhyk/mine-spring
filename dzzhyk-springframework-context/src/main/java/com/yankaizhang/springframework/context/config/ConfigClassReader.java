package com.yankaizhang.springframework.context.config;

import com.yankaizhang.springframework.beans.BeanDefinition;
import com.yankaizhang.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import com.yankaizhang.springframework.beans.factory.support.AbstractBeanDefinition;
import com.yankaizhang.springframework.context.BeanDefinitionRegistry;
import com.yankaizhang.springframework.context.annotation.ComponentScan;
import com.yankaizhang.springframework.context.annotation.Bean;
import com.yankaizhang.springframework.context.annotation.Lazy;
import com.yankaizhang.springframework.context.util.BeanDefinitionRegistryUtils;
import com.yankaizhang.springframework.util.StringUtils;

import java.lang.reflect.Method;


/**
 * 配置类解析器
 *
 * TODO: 这个类是在未完成前后置处理器之前对于配置类解析的替代品
 * @author dzzhyk
 */

public class ConfigClassReader {

    private BeanDefinitionRegistry registry;
    private ClassPathBeanDefinitionScanner scanner;

    public ConfigClassReader(BeanDefinitionRegistry registry, ClassPathBeanDefinitionScanner scanner) {
        this.registry = registry;
        this.scanner = scanner;
    }

    /**
     * 解析配置类，注册配置类中的Bean对象
     */
    public void parseAnnotationConfigClass(Class<?> clazz) {
        // 解析最高层类
        doParseAnnotationConfigClass(clazz);
        // 获取内部类
        Class<?>[] declaredClasses = clazz.getDeclaredClasses();
        for (Class<?> declaredClass : declaredClasses) {
            doParseAnnotationConfigClass(declaredClass);
        }
    }


    private void doParseAnnotationConfigClass(Class<?> configClass) {

        ComponentScan annotation = configClass.getAnnotation(ComponentScan.class);
        if (annotation!=null){
            // 添加包扫描路径
            String[] basePackages = annotation.value();
            scanner.scan(basePackages);
        }

        // 解析方法
        Method[] declaredMethods = configClass.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            if (declaredMethod.isAnnotationPresent(Bean.class)){
                // 解析BeanMethod
                parseBeanMethod(configClass, declaredMethod);
            }
        }
    }

    /**
     * 解析BeanMethod
     */
    private void parseBeanMethod(Class<?> configClass, Method method){

        // 配置类就是@Bean对象的工厂
        Class<?> beanClass = method.getReturnType();
        // factoryBeanName改为beanName形式
        String factoryBeanName = StringUtils.toLowerCase(configClass.getSimpleName());
        String beanName = method.getAnnotation(Bean.class).value();
        boolean lazyInit = (method.getAnnotation(Lazy.class)!=null);
        String factoryMethodName = method.getName();

        if ("".equals(beanName.trim())){
            beanName = factoryMethodName;
        }

        // 创建待注册的bean定义对象
        // 其实还可以添加很多属性，只不过那些注解都没实现
        AnnotatedGenericBeanDefinition beanDef = new AnnotatedGenericBeanDefinition(beanClass);
        beanDef.setFactoryBeanName(factoryBeanName);
        beanDef.setFactoryMethodName(factoryMethodName);
        beanDef.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        beanDef.setRole(BeanDefinition.ROLE_APPLICATION);
        beanDef.setLazyInit(lazyInit);

        BeanDefinitionRegistryUtils.registerBeanDefinition(registry, beanName, beanDef);
    }
}
