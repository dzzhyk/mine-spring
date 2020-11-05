package com.yankaizhang.springframework.core.util;

import org.junit.Test;

public class TestMultiValueMap {


    /**
     * 检测多值Map
     */
    @Test
    public void testLinkedMultiValueMap(){
        LinkedMultiValueMap<String, String> phones = new LinkedMultiValueMap<>();
        phones.add("dzzhyk", "123");
        phones.add("dzzhyk", "345");
        phones.add("dzzhyk", "234");
        phones.add("小明", "123456");
        phones.add("小明", "654321");
        System.out.println(phones);
        System.out.println(phones.get("小明").contains("123"));
    }

}
