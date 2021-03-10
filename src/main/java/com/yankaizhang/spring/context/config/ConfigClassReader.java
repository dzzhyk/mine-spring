package com.yankaizhang.spring.context.config;

import com.yankaizhang.spring.beans.BeanDefinition;
import com.yankaizhang.spring.beans.factory.impl.AnnotatedGenericBeanDefinition;
import com.yankaizhang.spring.beans.factory.support.AbstractBeanDefinition;
import com.yankaizhang.spring.beans.BeanDefinitionRegistry;
import com.yankaizhang.spring.context.annotation.Bean;
import com.yankaizhang.spring.context.annotation.ComponentScan;
import com.yankaizhang.spring.context.annotation.Lazy;
import com.yankaizhang.spring.context.util.BeanDefinitionRegistryUtils;
import com.yankaizhang.spring.util.StringUtils;

import java.lang.reflect.Method;


/**
 * 配置类解析器
 *
 * TODO: 这个类是在未完成前后置处理器之前对于配置类解析的替代品
 * @author dzzhyk
 * @since 2020-11-28 13:50:58
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

        Bean beanAnno = method.getAnnotation(Bean.class);
        String beanName = beanAnno.value();
        String initMethod = beanAnno.initMethod();
        String destroyMethod = beanAnno.destroyMethod();

        boolean lazyInit = (method.getAnnotation(Lazy.class)!=null);
        String factoryMethodName = method.getName();

        if (StringUtils.isEmpty(beanName)){
            beanName = factoryMethodName;
        }

        // 创建待注册的bean定义对象
        // 其实还可以添加很多属性，只不过有些注解都没实现，目前就实现了一个@Lazy
        AnnotatedGenericBeanDefinition beanDef = new AnnotatedGenericBeanDefinition(beanClass);
        beanDef.setFactoryBeanName(factoryBeanName);
        beanDef.setFactoryMethodName(factoryMethodName);
        beanDef.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        beanDef.setRole(BeanDefinition.ROLE_APPLICATION);
        beanDef.setLazyInit(lazyInit);
        beanDef.setInitMethodName(initMethod);
        beanDef.setDestroyMethodName(destroyMethod);

        BeanDefinitionRegistryUtils.registerBeanDefinition(registry, beanName, beanDef);
    }
}
