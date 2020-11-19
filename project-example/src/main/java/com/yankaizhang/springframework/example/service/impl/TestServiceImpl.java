package com.yankaizhang.springframework.example.service.impl;

import com.yankaizhang.springframework.context.annotation.Service;
import com.yankaizhang.springframework.example.service.TestService;

@Service
public class TestServiceImpl implements TestService {
    @Override
    public String sayHello(String name) {
        return "hello! " + name;
    }
}
