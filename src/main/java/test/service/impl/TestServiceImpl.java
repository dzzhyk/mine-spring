package test.service.impl;

import com.yankaizhang.springframework.annotation.Service;
import test.service.TestService;

@Service
public class TestServiceImpl implements TestService {
    @Override
    public String sayHello(String name) {
        int a = 1/0;
        return "哈哈！原来你就是那个" + name + "啊，你好你好！";
    }
}
