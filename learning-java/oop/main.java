package oop;


public class main {
    public static void main(String[] args) {
        Person person1 = new Person("Justice", "uzo", true);
        Person person2 = new Person("Justice", "chi", false);
        Person person3 = new Person("Jussy", "duru", true);
        
        System.out.println(person1.lastName);
        System.out.println(person1.isMale);
        person3.sayHello();
    }

     static class Person {
    String firstName;
    String lastName;
    boolean isMale;

    // here is called the contructor
    Person(String firstName,   String lastName, boolean isMale){
        this.firstName = firstName;
        this.lastName = lastName;
        this.isMale = isMale;
    }

    void sayHello(){
        System.out.println("hello " + this.firstName  + " " + this.lastName);

    }
    
  }
}
