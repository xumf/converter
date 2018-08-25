package main.java.net.xumf.demo;

import main.java.net.xumf.converter.Converter;
import main.java.net.xumf.demo.entity.KeyValue;
import main.java.net.xumf.demo.entity.Person;

public class SimpleDemo {

    public static void main(String[] args) {

        // 使用注解方式进行对象转换
        Person person = new Person();
        person.setUsername("Rose");
        person.setAge(12);
        person.setHobbies("吃饭");

        Person p2 = new Person();
        p2.setAge(13);
        Person to = Converter.to(p2, person);
        KeyValue keyValue = Converter.to(KeyValue.class, person);
//        System.out.println(keyValue.toString());
    }
}
