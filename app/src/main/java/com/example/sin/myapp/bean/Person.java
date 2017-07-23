package com.example.sin.myapp.bean;

import java.util.List;

/**
 * Created by sin on 2017/7/15.
 */

public class Person {
    public String city;
    public String sex;
    public int age;

    public Person(String city, String sex, int age) {
        this.city = city;
        this.sex = sex;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "city='" + city + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                '}';
    }

}
