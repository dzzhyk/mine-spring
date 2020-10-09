package test.service.impl;

import com.yankaizhang.springframework.annotation.Service;

import java.util.Random;

@Service
public class UserServiceImpl {

    public String helloUser(String username){
        return "你好！" + username + new Random();
    }

}
