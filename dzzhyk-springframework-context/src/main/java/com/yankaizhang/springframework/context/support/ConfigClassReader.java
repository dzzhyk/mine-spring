package com.yankaizhang.springframework.context.support;

import com.yankaizhang.springframework.context.config.AnnotatedBeanDefinitionReader;
import com.yankaizhang.springframework.beans.factory.support.BeanDefinition;
import com.yankaizhang.springframework.context.annotation.Bean;
import com.yankaizhang.springframework.context.annotation.Lazy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 配置类解析器
 */
public class ConfigClassReader {

    public static final Logger log = LoggerFactory.getLogger(AnnotatedBeanDefinitionReader.class);
    private AnnotatedBeanDefinitionReader reader;

    public ConfigClassReader(AnnotatedBeanDefinitionReader reader) {
        this.reader = reader;
    }

    /**
     * 解析配置类，注册配置类中的Bean对象
     */
    public Set<BeanDefinition> parseAnnotationConfigClass(Class<?> clazz) {
        Set<BeanDefinition> definitions = new HashSet<>(doParseAnnotationConfigClass(clazz));
        // 获取内部类
        Class<?>[] declaredClasses = clazz.getDeclaredClasses();
        for (Class<?> declaredClass : declaredClasses) {
            List<BeanDefinition> beanDefinitions = doParseAnnotationConfigClass(declaredClass);
            definitions.addAll(beanDefinitions);
        }
        return definitions;
    }


    private List<BeanDefinition> doParseAnnotationConfigClass(Class<?> configClass) {
        Method[] declaredMethods = configClass.getDeclaredMethods();
        ArrayList<BeanDefinition> definitions  = new ArrayList<>(declaredMethods.length);
        for (Method declaredMethod : declaredMethods) {
            if (declaredMethod.isAnnotationPresent(Bean.class)){
                // 解析BeanMethod
                definitions.add(parseBeanMethod(configClass, declaredMethod));
            }
        }
        return definitions;
    }

    /**
     * 解析BeanMethod
     */
    private BeanDefinition parseBeanMethod(Class<?> configClass, Method method){

        // 配置类就是@Bean对象的工厂
        String factoryBeanClassName = configClass.getName();
        String factoryBeanName = AnnotatedBeanDefinitionReader.toLowerCase(configClass.getSimpleName());

        String methodName = method.getAnnotation(Bean.class).value();
        String factoryMethodName = method.getName();  // 默认beanName是方法名
        if (!"".equals(methodName.trim())){
            factoryMethodName = methodName;
        }
        // 使用Bean工厂name信息和Bean工厂方法name信息创建BeanDefinition
        BeanDefinition definition = reader.doCreateConfigClassBeanDefinition(factoryBeanName, factoryBeanClassName,
                factoryMethodName, method.isAnnotationPresent(Lazy.class));

        // 设置这个BeanDefinition对象最终获得的beanClassName，就是方法的返回类型名称
        String returnBeanClassName = method.getReturnType().getName();
        definition.setBeanClassName(returnBeanClassName);
        return definition;
    }


}
