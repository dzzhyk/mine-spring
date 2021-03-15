package com.test;

import com.yankaizhang.spring.beans.factory.annotation.Autowired;
import com.yankaizhang.spring.context.annotation.Component;

@Component
public class MyComponent {

    @Autowired
    private Car car;

    @Override
    public String toString() {
        return "MyComponent{" +
                "car=" + car +
                '}';
    }

    private void initMyComponent(){
        System.out.println("MyComponent -> 初始化方法");
    }

    private void destroyMyComponent(){
        System.out.println("MyComponent -> 销毁方法");
    }
}
