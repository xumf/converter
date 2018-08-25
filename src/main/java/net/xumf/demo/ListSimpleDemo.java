package main.java.net.xumf.demo;

import main.java.net.xumf.converter.Converter;
import main.java.net.xumf.demo.entity.KeyValue;
import main.java.net.xumf.demo.entity.Person;

import java.util.ArrayList;
import java.util.List;

public class ListSimpleDemo {

    public static void main(String[] args) {

        // 使用注解方式进行对象转换
        Person person = new Person();
        person.setUsername("Rose");
        person.setAge(12);
        person.setHobbies("吃饭");

        // 使用注解方式进行对象转换
        Person person2 = new Person();
        person2.setUsername("Tom");
        person2.setAge(18);
        person2.setHobbies("足球");

        List<Person> personList = new ArrayList<>();
        personList.add(person);
        personList.add(person2);

        List<KeyValue> keyValueList = Converter.toList(KeyValue.class, personList);

        KeyValue keyValue = Converter.to(KeyValue.class, person);

        System.out.println(keyValue.toString());
    }
}
