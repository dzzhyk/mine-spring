package com.test;

import com.yankaizhang.spring.beans.factory.config.DestructionAwareBeanPostProcessor;
import com.yankaizhang.spring.context.annotation.Component;

@Component
public class MyDestructionProcessor implements DestructionAwareBeanPostProcessor {
    @Override
    public void postProcessBeforeDestruction(Object bean, String beanName) throws Exception {
        if (bean instanceof MyComponent) {
            System.out.println("DestructionProcessor -> 销毁之前");
        }
    }
}
