package com.yankaizhang.spring.example.model;

import lombok.Data;

/**
 * @author dzzhyk
 */

@Data
public class Car {

    private String brand;

    public Car(String brand) {
        this.brand = brand;
    }

    public void carInit(){
        System.out.println("carInit 自定义初始化方法");
    }

    public void carDestroy(){
        System.out.println("carDestroy 自定义销毁方法");
    }
}
