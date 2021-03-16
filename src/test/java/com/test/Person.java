package com.test;

public class Person {

    private String name;
    private int age;
    private boolean sex;

    public Person() {
    }

    public Person(String name, int age, boolean sex) {
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }


    public void initTestPerson(){
        System.out.println("Person的初始化函数");
    }

    public void destroyTestPerson(){
        System.out.println("Person的销毁函数");
    }

    @Override
    public String toString() {
        return "com.test.Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", sex=" + sex +
                '}';
    }
}
