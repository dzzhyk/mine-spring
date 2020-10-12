package com.yankaizhang.springframework.beans.support;

import com.yankaizhang.springframework.beans.config.BeanDefinition;
import com.yankaizhang.springframework.context.ApplicationContext;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * BeanDefinitionReader主要完成对application.properties配置文件的解析
 */
public class BeanDefinitionReader {

    public static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BeanDefinitionReader.class);

    private List<String> registryBeanClasses = new ArrayList<>();
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

                result.add(doCreateBeanDefinition(toLowerCase(clazz.getSimpleName()), clazz.getName()));

                // 接口的bean定义
                for (Class<?> i : clazz.getInterfaces()) {
                    result.add(doCreateBeanDefinition(toLowerCase(i.getSimpleName()), clazz.getName()));
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将某个beanName创建为BeanDefinition对象
     */
    private BeanDefinition doCreateBeanDefinition(String factoryBeanName, String beanClassName){
        log.debug("创建bean定义: [factoryBeanName ==> "+ factoryBeanName + "] [beanClassName ==> " + beanClassName + "]");
        return new BeanDefinition(beanClassName, factoryBeanName);
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
