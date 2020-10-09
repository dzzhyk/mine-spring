package test.service.impl;

import com.yankaizhang.springframework.annotation.Service;
import lombok.extern.slf4j.Slf4j;
import test.service.TestService;

@Service
@Slf4j
public class TestServiceImpl implements TestService {
    @Override
    public String sayHello(String name) {
        log.info("hello函数体执行...");
        return "哈哈！原来你就是那个" + name + "啊，你好你好！";
    }
}
