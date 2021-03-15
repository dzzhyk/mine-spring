package com.test;

import com.yankaizhang.spring.beans.PropertyValues;
import com.yankaizhang.spring.beans.factory.config.InstantiationAwareBeanPostProcessor;
import com.yankaizhang.spring.context.annotation.Component;

@Component
public class MyInstantiationProcessor implements InstantiationAwareBeanPostProcessor {
    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws RuntimeException {
        if (beanClass.equals(MyComponent.class)){
            System.out.println("InstantiationProcessor -> 实例化之前");
        }
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws RuntimeException {
        if (bean instanceof MyComponent) {
            System.out.println("InstantiationProcessor -> 实例化之后");
        }
        return false;
    }

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws RuntimeException {
        if (bean instanceof MyComponent) {
            System.out.println("InstantiationProcessor -> 编辑属性");
        }
        return pvs;
    }


}
