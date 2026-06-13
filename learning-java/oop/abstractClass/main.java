package oop.abstractClass;

public class main {
    public static void main(String[] args){
        /*ok. the first thing you have to know about abstract class is you can not instantiate it.
        * but you can make subclasess of an abstract class. A method can also be abstract and just like interface
        * tha methods do not have bodies. what abstract class does is to enforec and organise what every
        * subclass has to have*/

        Cat cat = new Cat();
        cat.makenoise();
    }
}
