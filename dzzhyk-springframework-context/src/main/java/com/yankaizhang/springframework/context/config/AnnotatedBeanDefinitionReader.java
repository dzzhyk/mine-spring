package com.yankaizhang.springframework.context.config;

import com.yankaizhang.springframework.aop.support.AopUtils;
import com.yankaizhang.springframework.beans.factory.support.RootBeanDefinition;
import com.yankaizhang.springframework.context.annotation.Component;
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
 * AnnotatedBeanDefinitionReader主要完成对注解配置文件的解析
 */
public class AnnotatedBeanDefinitionReader {

    public static final Logger log = LoggerFactory.getLogger(AnnotatedBeanDefinitionReader.class);

    private List<String> registryBeanClasses = new ArrayList<>(16);
    private Properties config = new Properties();
    private final String SCAN_PACKAGE = "scanPackage";  // 设置配置文件Key
    private Set<RootBeanDefinition> alreadyRegistered = new CopyOnWriteArraySet<>();  // 已经注册的BeanDefinition

    /**
     * 通过构造方法获取从ApplicationContext传入的locations路径，然后解析扫描和保存相关所有的类并且提供统一的访问入口
     */
    public AnnotatedBeanDefinitionReader(String... locations) {
        InputStream ins = this.getClass()
                .getClassLoader().getResourceAsStream(locations[0].replace("classpath:", ""));
        try {
            config.load(ins);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (null != ins){
                try { ins.close(); } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        doScanner(config.getProperty(SCAN_PACKAGE));
    }

    /**
     * 根据读取到了配置包位置信息获取真正的url
     * 获取这些类的全包名
     */
    private void doScanner(String scanPackage){
        URL url = this.getClass().getClassLoader().getResource(scanPackage.replaceAll("\\.", "/"));
        if (url == null){return;}
        File classDir = new File(url.getFile());
        for (File file : classDir.listFiles()) {
            if (file.isDirectory()) {
                // 对子目录循环调用
                doScanner(scanPackage + "." + file.getName());
            } else {
                if (!file.getName().endsWith(".class")){continue;}
                String className = (scanPackage + "." + file.getName().replace(".class", ""));
                registryBeanClasses.add(className);
            }
        }
    }

    /**
     * 将扫描到的位置信息转换为BeanDefinition对象，便于之后的IoC操作
     */
    public List<RootBeanDefinition> loadBeanDefinitions(){
        List<RootBeanDefinition> result = new ArrayList<>();
        try {
            for (String className : registryBeanClasses) {
                Class<?> clazz = Class.forName(className);

                if (clazz.isInterface()) continue;  // 如果clazz是接口就撤

                boolean isComponent = false;
                for (Annotation annotation : clazz.getAnnotations()) {
                    Class<?> target = AopUtils.getAnnotationJdkProxyTarget(annotation);
                    if (target==null){
                        continue;
                    }
                    if (target == Component.class || target.isAnnotationPresent(Component.class)){
                        isComponent = true;
                        break;
                    }
                }

                // 如果是组件，需要注册Bean定义
                if (isComponent){
                    // 默认关闭懒加载
                    result.add(doCreateBeanDefinition(toLowerCase(clazz.getSimpleName()), clazz.getName(), false));

                    // 接口的bean定义
                    for (Class<?> i : clazz.getInterfaces()) {
                        result.add(doCreateBeanDefinition(toLowerCase(i.getSimpleName()), clazz.getName(), false));
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 创建BeanDefinition对象
     */
    private RootBeanDefinition doCreateBeanDefinition(String factoryBeanName, String beanClassName, boolean lazy){
        log.debug("创建bean定义:[普通反射] [factoryBeanName ==> "+ factoryBeanName + "] [beanClassName ==> " + beanClassName + "]");
        RootBeanDefinition definition = new RootBeanDefinition(beanClassName, factoryBeanName, lazy);
        return definition;
    }


    /**
     * 创建BeanDefinition对象
     */
    public RootBeanDefinition doCreateConfigClassBeanDefinition(String factoryBeanName, String factoryBeanClassName,
                                                                String factoryMethodName, boolean lazy){
        log.debug("创建bean定义:[工厂方式] [factoryBeanName ==> "+ factoryBeanName + "] [factoryMethodName ==> " + factoryMethodName + "]");
        RootBeanDefinition beanDefinition = new RootBeanDefinition(factoryBeanName, factoryMethodName);
        beanDefinition.setFactoryBeanClassName(factoryBeanClassName);
        beanDefinition.setLazyInit(lazy);
        return beanDefinition;
    }

    /**
     * 将首字母小写
     */
    public static String toLowerCase(String string){
        char[] chars = string.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    public Properties getConfig() {
        return config;
    }

    public boolean checkAlreadyRegistered(RootBeanDefinition definition){
        return alreadyRegistered.contains(definition);
    }

    public void markRegistered(RootBeanDefinition beanDefinition){
        this.alreadyRegistered.add(beanDefinition);
    }
}
