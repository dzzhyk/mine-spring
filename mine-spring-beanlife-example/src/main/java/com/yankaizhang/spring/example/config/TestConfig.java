package com.yankaizhang.spring.example.config;

import com.yankaizhang.spring.context.annotation.Bean;
import com.yankaizhang.spring.context.annotation.Configuration;
import com.yankaizhang.spring.example.model.Car;

/**
 * @author dzzhyk
 * 测试配置类
 */
@Configuration
public class TestConfig {

    @Bean(initMethod = "carInit", destroyMethod = "carDestroy")
    public Car myAtto(){
        return new Car("atto");
    }

}
