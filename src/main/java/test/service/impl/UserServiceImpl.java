package test.service.impl;

import com.yankaizhang.springframework.annotation.Service;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@Slf4j
public class UserServiceImpl {

    public String helloUser(String username){
        log.info("helloUser函数体执行...");
        return "你好！" + username + LocalDateTime.now();
    }

}
