package com.yankaizhang.spring.context.util;

import com.yankaizhang.spring.beans.BeanDefinition;
import com.yankaizhang.spring.beans.factory.support.BeanDefinitionRegistry;
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
                                              String beanName,
                                              BeanDefinition beanDefinition){
        try {
            // 根据beanName注册
            if (!registry.isBeanNameInUse(beanName)){
                // 调用容器注册这个定义
                registry.registerBeanDefinition(beanName, beanDefinition);
                if (log.isDebugEnabled()){
                    log.debug("创建bean定义 : ["+ beanName + "] => " + beanDefinition.getBeanClassName());
                }
            }else{
                // 存在重复beanName
                log.debug("存在重复bean定义 : " + beanName + ", 跳过注册");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
