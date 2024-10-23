package lamda;

public class main {
    interface Animal {

        String speak(int animalNumber);
    }


    public static void main(String[] args) {
        /*lamda can only be use on interfaces*/
        /*here i do not need to create another class that will implement the interfaces for my to be able
        * to get the interface working. i just instantiate with lamda the call the methods i want*/

       Animal cat = (n) -> "cat number " +n+ "  goes meow";
        System.out.println(cat.speak(2));
    }
}
