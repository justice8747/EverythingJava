package methods;

public class methods {
    public static void main(String[] args) {
        String name = "Justice";
        int age = 45;
        func();
        System.out.println(fun2());
        fun1(name, age);
    }

    /* java mothods is just like javascript function. they are basically the same thing with
     * the almost thesame syntax the only difference is the clearation. while you write 
     * the keyword fiunction when declearing a function in javascript, you write the keyword
     * void when declearing a java method and this is for the method that will not be returning
     * anything
     */

    static void func(){
    System.out.println("Hello, World!");
}


    /* and to pass argurment to the method you just have to do it like you would in 
     * javascript but with the data type
     */



     static void fun1(String name, int age){
    System.out.println("Hello, " +name+ " you are " +age+ " years old");

}

/* for methods that will return something, you dont need to add the void keyword again
 * you just declear the type of what the method will be returing
 */

    static int fun2(){
        return 10;
    }

}
