package com.yankaizhang.spring.context.config;

import com.yankaizhang.spring.beans.BeanDefinition;
import com.yankaizhang.spring.beans.BeanDefinitionRegistry;
import com.yankaizhang.spring.beans.factory.BeanFactory;
import com.yankaizhang.spring.beans.factory.config.BeanDefinitionRegistryPostProcessor;
import com.yankaizhang.spring.beans.factory.impl.AnnotatedGenericBeanDefinition;
import com.yankaizhang.spring.context.annotation.*;
import com.yankaizhang.spring.context.util.BeanDefinitionRegistryUtils;
import com.yankaizhang.spring.util.AnnotationUtils;
import com.yankaizhang.spring.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

import static com.yankaizhang.spring.beans.BeanDefinition.SCOPE_SINGLETON;

/**
 * 配置类解析器，实现了{@link BeanDefinitionRegistryPostProcessor}接口，在容器创建之后使用
 * @author dzzhyk
 * @since 2021-03-13 11:59:37
 */
public class ConfigurationClassPostProcessor implements BeanDefinitionRegistryPostProcessor {

    public static final Logger log = LoggerFactory.getLogger(ConfigurationClassPostProcessor.class);

    private BeanDefinitionRegistry registry;
    private ClassPathBeanDefinitionScanner scanner;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
        this.registry = registry;
        this.scanner  = new ClassPathBeanDefinitionScanner(this.registry);
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
                    parseAnnotationConfigClass(configClazz);
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
        // 解析@ComponentScan注解
        ComponentScan componentScanAnnotation = configClass.getAnnotation(ComponentScan.class);
        if (componentScanAnnotation!=null){
            // 添加包扫描路径
            String[] basePackages = componentScanAnnotation.value();
            scanner.scan(basePackages);
        }

        // 解析@Import注解
        Set<Class<?>> visitedSet = new LinkedHashSet<>();
        parseImport(configClass, visitedSet);

        // 解析@Bean注解
        Method[] declaredMethods = configClass.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            if (declaredMethod.isAnnotationPresent(Bean.class)){
                // 解析BeanMethod
                parseBeanMethod(configClass, declaredMethod);
            }
        }
    }


    /**
     * 递归地解析配置类所标注的所有Import注解
     */
    private void parseImport(Class<?> configClass, Set<Class<?>> visitedSet){
        // 使用set来避免一直递归
        if (visitedSet.add(configClass)){
            for (Annotation annotation : configClass.getAnnotations()) {
                Class<?> annotationClazz = AnnotationUtils.getAnnotationJdkProxyTarget(annotation);
                if (Import.class.isAssignableFrom(annotationClazz)){
                    Import importAnnotation = ((Import) annotation);
                    Class<?>[] classes = importAnnotation.value();
                    for (Class<?> clazz : classes) {
                        AnnotatedGenericBeanDefinition beanDef = new AnnotatedGenericBeanDefinition(clazz);
                        String beanClassName = beanDef.getBeanClassName();
                        String beanName = StringUtils.toLowerCase(beanClassName.substring(beanClassName.lastIndexOf(".")+1));
                        BeanDefinitionRegistryUtils.registerBeanDefinition(registry, beanName, beanDef);

                        // 如果Import之后是个配置类，就继续解析配置类
                        if (clazz.isAnnotationPresent(Configuration.class)){
                            parseAnnotationConfigClass(clazz);
                        }
                    }
                }else{
                    // 递归地查找所有注解里面的@Import注解
                    parseImport(annotationClazz, visitedSet);
                }
            }
        }
    }


    /**
     * 解析BeanMethod
     */
    private void parseBeanMethod(Class<?> configClass, Method method){

        // 配置类就是@Bean对象的工厂
        Class<?> beanClass = method.getReturnType();

        if (Void.class == beanClass || void.class == beanClass){
            log.warn("不允许配置void或者Void类型bean对象，忽略该@Bean配置 => {}", method.getName());
            return;
        }

        // factoryBeanName改为beanName形式
        String factoryBeanName = StringUtils.toLowerCase(configClass.getSimpleName());

        Bean beanAnno = method.getAnnotation(Bean.class);
        String beanName = beanAnno.value();
        String initMethod = beanAnno.initMethod();
        String destroyMethod = beanAnno.destroyMethod();
        boolean lazyInit = (method.getAnnotation(Lazy.class)!=null);
        // 解析bean作用域，默认为单例
        String scopeName = SCOPE_SINGLETON;
        Scope scope = method.getAnnotation(Scope.class);
        if(scope != null){
            scopeName = scope.value();
        }
        String factoryMethodName = method.getName();

        if (StringUtils.isEmpty(beanName)){
            beanName = factoryMethodName;
        }

        // 创建待注册的bean定义对象
        // 其实还可以添加很多属性，只不过有些注解都没实现，目前就实现了一个@Lazy
        AnnotatedGenericBeanDefinition beanDef = new AnnotatedGenericBeanDefinition(beanClass);
        beanDef.setFactoryBeanName(factoryBeanName);
        beanDef.setFactoryMethodName(factoryMethodName);
        beanDef.setRole(BeanDefinition.ROLE_APPLICATION);
        beanDef.setLazyInit(lazyInit);
        beanDef.setInitMethodName(initMethod);
        beanDef.setDestroyMethodName(destroyMethod);
        beanDef.setScope(scopeName);
        BeanDefinitionRegistryUtils.registerBeanDefinition(registry, beanName, beanDef);
    }
}
