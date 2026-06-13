package oop.Interfaces;

public class main {
    public static void main(String[] args){
        /*interfaces is just a template that can be applied to a class. it is similar to inheritance.
        * classes can apply more than interface unlike inheritance which can only apply one*/

        /*the main key difference between interface and abstract class is:
        * 1, you can implement as many interface you want whereas in abstract class tou can only extend one class.
        * 2, in interface if you declare any field it is going to be static and final. what that means is i declares any
        * variable in interface i must assign it a value and it will be the same in every class that implement it.
        * i.e i can not change the value of the variable after assignment.*/

        Rabbit  rabbit = new Rabbit();
        rabbit.flee();

        hawk Hawk = new hawk();
        Hawk.hunt();

        Fish fish = new Fish();
        fish.flee();
        fish.hunt();
        System.out.println(4%2);
    }
}
