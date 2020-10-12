package com.yankaizhang.springframework.test.service.impl;

import com.yankaizhang.springframework.annotation.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl {

    public String helloUser(String username){
        return "你好！" + username + LocalDateTime.now();
    }

}
