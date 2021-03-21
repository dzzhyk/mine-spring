package com.component;

import com.yankaizhang.spring.beans.factory.annotation.Autowired;
import com.yankaizhang.spring.context.annotation.Component;

@Component
public class CompB {


    @Autowired
    private CompA compA;

    @Override
    public String toString() {
        return "CompB{" +
                "compA=" + compA.getClass() +
                '}' + "BBBBB";
    }

    public CompA getCompA() {
        return compA;
    }
}
