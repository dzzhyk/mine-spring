package com.yankaizhang.springframework.beans.factory.config;

import com.yankaizhang.springframework.beans.MutablePropertyValues;
import com.yankaizhang.springframework.beans.factory.support.BeanDefinition;
import com.yankaizhang.springframework.context.annotation.Bean;
import com.yankaizhang.springframework.context.annotation.Configuration;
import com.yankaizhang.springframework.context.annotation.Lazy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * BeanDefinitionReader主要完成对application.properties配置文件的解析
 */
public class BeanDefinitionReader {

    public static final Logger log = LoggerFactory.getLogger(BeanDefinitionReader.class);

    private List<String> registryBeanClasses = new ArrayList<>(4);
    private Properties config = new Properties();
    private final String SCAN_PACKAGE = "scanPackage";  // 设置配置文件Key

    /**
     * 通过构造方法获取从ApplicationContext传入的locations路径，然后解析扫描和保存相关所有的类并且提供统一的访问入口
     */
    public BeanDefinitionReader(String... locations) {
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
    public List<BeanDefinition> loadBeanDefinitions(){
        List<BeanDefinition> result = new ArrayList<>();
        try {
            for (String className : registryBeanClasses) {
                Class<?> clazz = Class.forName(className);

                if (clazz.isInterface()) continue;  // 如果clazz是接口就撤

                // 如果这个类是配置类，解析这个配置类，并且加载这个配置类里定义的bean的BeanDefinition
                if (clazz.isAnnotationPresent(Configuration.class)){
                     List<BeanDefinition> AnnotationBeanDefinitions = parseAnnotationConfig(clazz);
                     result.addAll(AnnotationBeanDefinitions);
                    continue;
                }

                // 默认关闭懒加载
                result.add(doCreateBeanDefinition(toLowerCase(clazz.getSimpleName()), clazz.getName(), false));

                // 接口的bean定义
                for (Class<?> i : clazz.getInterfaces()) {
                    result.add(doCreateBeanDefinition(toLowerCase(i.getSimpleName()), clazz.getName(), false));
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 解析配置类，注册配置类中的Bean对象
     */
    private List<BeanDefinition> parseAnnotationConfig(Class<?> clazz) {
        List<BeanDefinition> definitions = new ArrayList<>(doParseAnnotationConfig(clazz));
        // 获取内部类
        Class<?>[] declaredClasses = clazz.getDeclaredClasses();
        for (Class<?> declaredClass : declaredClasses) {
            List<BeanDefinition> beanDefinitions = doParseAnnotationConfig(declaredClass);
            definitions.addAll(beanDefinitions);
        }
        return definitions;
    }

    private List<BeanDefinition> doParseAnnotationConfig(Class<?> clazz) {
        Method[] declaredMethods = clazz.getDeclaredMethods();
        ArrayList<BeanDefinition> definitions  = new ArrayList<>(declaredMethods.length);
        for (Method declaredMethod : declaredMethods) {
            if (declaredMethod.isAnnotationPresent(Bean.class)){
                definitions.add(parseBeanMethod(declaredMethod));
            }
        }
        return definitions;
    }

    /**
     * 解析@Bean方法
     */
    private BeanDefinition parseBeanMethod(Method method){
        Class<?> returnType = method.getReturnType();
        String beanName = method.getAnnotation(Bean.class).value();
        String factoryBeanName = method.getName();  // 默认beanName是方法名
        if (!"".equals(beanName.trim())){
            factoryBeanName = beanName;
        }
        BeanDefinition beanDefinition =
                doCreateBeanDefinition(factoryBeanName, returnType.getName(), method.isAnnotationPresent(Lazy.class));
        MutablePropertyValues mutablePropertyValues = new MutablePropertyValues();
        // 一开始的属性是空的
        beanDefinition.setPropertyValues(mutablePropertyValues);
        return beanDefinition;
    }


    /**
     * 将某个beanName创建为BeanDefinition对象
     */
    private BeanDefinition doCreateBeanDefinition(String factoryBeanName, String beanClassName, boolean lazy){
        log.debug("创建bean定义: [factoryBeanName ==> "+ factoryBeanName + "] [beanClassName ==> " + beanClassName + "]");
        return new BeanDefinition(beanClassName, factoryBeanName, lazy);
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
}
