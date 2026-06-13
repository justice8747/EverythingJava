package oop.encapsulation;

public class main {
    public static void main(String[] args){
        /*this is nasically makinf the attribute or variables of a class private to that class alone. like you wont be able to
        * interact with it outside that class. well you can only be able to interact with it with getters and setters*/
        Student student1 = new Student("justice",  56, "male");

        student1.setage(45);
        student1.setName("jussy");
        student1.setgender("male");

        System.out.println(student1.getName());

    }
}
