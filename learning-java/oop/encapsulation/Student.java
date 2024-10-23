package oop.encapsulation;

public class Student {
    private String name;
    private String gender;
    private int age;

    // contructor
    public Student(String name, int age, String gender){
        this.setName(name);
        this.setgender(gender);
        this.setage(age);
    }

    //setters
    public void setName(String name){
        this.name = name;
    }

   public void setage(int age){
        this.age = age;
    }

   public void setgender(String gender){
        this.gender = gender;
    }

    //getters
    public String getName(){
        return name;
    }

    public int getAge(){
        return age;
    }

    public String getGender(){
        return gender;
    }


}
