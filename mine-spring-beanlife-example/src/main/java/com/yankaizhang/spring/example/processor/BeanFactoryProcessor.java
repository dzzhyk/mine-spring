package com.yankaizhang.spring.example.processor;

import com.yankaizhang.spring.beans.factory.BeanFactory;
import com.yankaizhang.spring.beans.factory.config.BeanFactoryPostProcessor;
import com.yankaizhang.spring.context.annotation.Component;


/**
 * @author dzzhyk
 * 对BeanFactory的后置处理
 */
@Component
public class BeanFactoryProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(BeanFactory beanFactory) throws RuntimeException {
        System.out.println("-------- BeanFactoryProcessor 处理 - 所有bean定义已经注册完成，此处允许修改 --------");
    }
}
