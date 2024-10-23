package oop.Interfaces;

public class Fish implements Prey, Preditor{
    /*here, this class is appling both the prey and preditor interfaces*/
    @Override
    public void flee(){
        System.out.println("this fish is fleeing from shark");
    }

    @Override
    public void hunt(){
        System.out.println("this fish is hunting smaller fishes");
    }

}
