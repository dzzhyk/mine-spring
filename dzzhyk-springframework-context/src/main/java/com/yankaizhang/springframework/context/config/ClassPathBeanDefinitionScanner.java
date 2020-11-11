package com.yankaizhang.springframework.context.config;

import com.yankaizhang.springframework.beans.factory.support.GenericBeanDefinition;
import com.yankaizhang.springframework.context.BeanDefinitionRegistry;
import com.yankaizhang.springframework.context.util.BeanDefinitionRegistryUtils;
import com.yankaizhang.springframework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.*;

/**
 * BeanDefinition扫描器，就是用来扫描包的，每根据包路径扫描到一个类class，就为这个类生成全类名并且加入registryBeanClasses
 * 光是扫描到包下所有的类名还不够，需要遍历所有的类，看看他是不是用注解定义的组件才行
 * 比如@Component，@Repository，@Service，@Controller
 * 这样过滤之后，再注册所有的结果，返回注册后的BeanDefinition列表
 *
 * 这个类在原有的Spring里面也存在继承关系，比较复杂
 * 因为我们目前只向实现Java配置类的容器和解析实现，这里我将简化实现这个扫描类，所以后面还可以继续改进和拓展
 * @author dzzhyk
 */

public class ClassPathBeanDefinitionScanner {

    public static final Logger log = LoggerFactory.getLogger(ClassPathBeanDefinitionScanner.class);

    /**
     * 这里的registry其实就是IoC容器对象
     */
    private BeanDefinitionRegistry registry;
    /**
     * 扫描到的类名结果
     */
    private Set<String> registryBeanClasses = new HashSet<>(16);

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    /**
     * 扫描所有basePackages
     */
    public void scan(String... basePackages){
        for (String basePackage : basePackages) {
            doScan(basePackage);
        }
        // 这里为了简化先不注册只生成BeanDefinition对象
        doRegisterBeanDefinition(this.registryBeanClasses);
    }

    private void doScan(String basePackage){
        URL url = this.getClass().getClassLoader().getResource(basePackage.replaceAll("\\.", "/"));
        if (url == null){return;}
        File classDir = new File(url.getFile());
        for (File file : classDir.listFiles()) {
            if (file.isDirectory()) {
                // 对子目录循环调用
                doScan(basePackage + "." + file.getName());
            } else {
                if (!file.getName().endsWith(".class")){continue;}
                String className = (basePackage + "." + file.getName().replace(".class", ""));
                registryBeanClasses.add(className);
                if (log.isDebugEnabled()){
                    log.debug("包扫描命中类 : " + className);
                }
            }
        }
    }

    /**
     * 注册扫描到的BeanDefinition
     *
     */
    public void doRegisterBeanDefinition(Set<String> registryBeanClasses) {

        for (String registryBeanClass : registryBeanClasses) {
            GenericBeanDefinition beanDef = new GenericBeanDefinition();

            // 这里放入的就是一个String对象
            beanDef.setBeanClass(registryBeanClass);

            // 扫描到的类的默认的名称是 开头小写开头小写的类名
            String beanName = StringUtils.toLowerCase(registryBeanClass.substring(registryBeanClass.lastIndexOf(".")+1));

            BeanDefinitionRegistryUtils.registerBeanDefinition(registry, beanName, beanDef);
        }

    }
}
