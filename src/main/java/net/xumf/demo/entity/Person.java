package main.java.net.xumf.demo.entity;

import main.java.net.xumf.converter.ConvertAnnotation;

public class Person {

    // 用户名
    @ConvertAnnotation("value")
    private String username;

    // 年龄
    @ConvertAnnotation("key")
    private Integer age;

    // 爱好
    private String hobbies;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }
}
