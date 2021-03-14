package com.yankaizhang.spring.context.util;

import com.yankaizhang.spring.beans.BeanDefinition;
import com.yankaizhang.spring.beans.BeanDefinitionRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 注册BeanDefinition的工具类
 * @author dzzhyk
 * @since 2020-11-28 13:51:18
 */

public class BeanDefinitionRegistryUtils {

    private static final Logger log = LoggerFactory.getLogger(BeanDefinitionRegistryUtils.class);

    public static void registerBeanDefinition(BeanDefinitionRegistry registry,
                                              String beanDefName,
                                              BeanDefinition beanDefinition){
        try {
            registry.registerBeanDefinition(beanDefName, beanDefinition);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
