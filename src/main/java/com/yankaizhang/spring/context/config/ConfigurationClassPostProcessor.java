package com.yankaizhang.spring.context.config;

import com.yankaizhang.spring.beans.BeanDefinition;
import com.yankaizhang.spring.beans.BeanDefinitionRegistry;
import com.yankaizhang.spring.beans.factory.BeanFactory;
import com.yankaizhang.spring.beans.factory.config.BeanDefinitionRegistryPostProcessor;
import com.yankaizhang.spring.beans.factory.impl.AnnotatedGenericBeanDefinition;
import com.yankaizhang.spring.beans.factory.support.AbstractBeanDefinition;
import com.yankaizhang.spring.context.annotation.Bean;
import com.yankaizhang.spring.context.annotation.ComponentScan;
import com.yankaizhang.spring.context.annotation.Configuration;
import com.yankaizhang.spring.context.annotation.Lazy;
import com.yankaizhang.spring.context.util.BeanDefinitionRegistryUtils;
import com.yankaizhang.spring.util.StringUtils;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 配置类解析器，实现了{@link BeanDefinitionRegistryPostProcessor}接口，在容器创建之后使用
 * @author dzzhyk
 */
public class ConfigurationClassPostProcessor implements BeanDefinitionRegistryPostProcessor {

    private ConfigClassReader configClassReader;
    private BeanDefinitionRegistry registry;
    private ClassPathBeanDefinitionScanner scanner;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
        this.registry = registry;
        this.scanner  = new ClassPathBeanDefinitionScanner(this.registry);
        this.configClassReader = new ConfigClassReader(this.registry, this.scanner);
        doProcessAnnotationConfiguration();
    }

    /**
     * 预先处理配置类的bean定义
     * Spring中这里使用的是一个BeanDefinitionRegisterPostProcessor来完成的
     */
    private void doProcessAnnotationConfiguration() {
        try {
            Map<String, BeanDefinition> temp = new LinkedHashMap<>(registry.getBeanDefinitionMap());
            for (Map.Entry<String, BeanDefinition> entry : temp.entrySet()) {
                BeanDefinition definition = entry.getValue();
                Class<?> configClazz = Class.forName(definition.getBeanClassName());
                // 如果是配置类，就解析该配置类下的@Bean注册内容
                if (configClazz.isAnnotationPresent(Configuration.class)) {
                    configClassReader.parseAnnotationConfigClass(configClazz);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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

        if ("".equals(beanName.trim())){
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

    @Override
    public void postProcessBeanFactory(BeanFactory beanFactory) throws RuntimeException {

    }
}
