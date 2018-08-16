package lesson_3.Dop_DZ;

import java.io.Serializable;

public class Student implements Serializable {
    private String name;
    private int age;
    private char sex;

    public Student(String name, int age, char sex) {
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    public void display(){
        System.out.println("Name - " + this.name + ". Age - " + this.age + ". Sex - " + this.sex);
    }
}
