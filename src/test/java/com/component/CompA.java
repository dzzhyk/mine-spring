package com.component;


import com.yankaizhang.spring.beans.factory.annotation.Autowired;
import com.yankaizhang.spring.context.annotation.Component;
import com.yankaizhang.spring.context.annotation.Scope;

@Component
public class CompA {

    @Autowired
    private CompB compB;

    @Override
    public String toString() {
        return "CompA{" +
                "compB=" + compB.getClass() +
                '}' + "AAAAA";
    }

    public void hello(){
        System.out.println("I'm AAAAA");
    }
}
