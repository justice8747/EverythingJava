package scannerClasses;
import java.util.Scanner;
/*
this is method is use to read user import. this works for the data types but there are methods for int, long,
 char and for all datatypes. and this will allow return a boolean.
*/
public class hasNext {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
       System.out.println("enter any word");
        //    this will return true or false depending on if there is another value
      Boolean nextValue = sc.hasNext();
     System.out.println(nextValue);
      String value = sc.next();
/*
      note that the next() method will stop reading when it encounted a white space.
      you can use nextLine if you want to read a complete line. the nextLine will stop
      reading when encounter backslash or newline
*/
        String nextLineValue =  sc.nextLine();


        System.out.println(nextLineValue);
       System.out.println(value);

        /*a practical usage of hasNext will be to check imput datatypes. if the specified datatype is not
        * the one the is entered ny the user it will return an error*/

//        this will check whether an input is and integer or not
       System.out.println("enter a Number");
        Boolean num = sc.hasNextInt();

        if(num){
            int numValue = sc.nextInt();
            System.out.println(numValue);
        }else{
            System.out.println("invalid imput");
        }
        System.out.println(num);

    }
}
