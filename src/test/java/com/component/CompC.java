package com.component;

import com.yankaizhang.spring.beans.factory.annotation.Autowired;
import com.yankaizhang.spring.context.annotation.Component;

@Component
public class CompC {

    private final String name = "cool";

    @Autowired
    private CompC compC;

    public void hello(){
        System.out.println(name);
    }
}
