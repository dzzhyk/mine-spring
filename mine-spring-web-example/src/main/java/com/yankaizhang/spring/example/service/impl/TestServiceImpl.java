package com.yankaizhang.spring.example.service.impl;

import com.yankaizhang.spring.context.annotation.Service;
import com.yankaizhang.spring.example.service.TestService;

/**
 * 测试服务类
 * @author dzzhyk
 */
@Service
public class TestServiceImpl implements TestService {

    @Override
    public String sayHello(String name) {
        return "hello, " + name;
    }

}
