package com.bdsoft.pojo;

/**
 * 功能
 *
 * @author 丁辰叶
 * @version 1.0
 * @date 2018/5/21 15:15
 */
public class User {

    private String name;
    private Integer age;

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
