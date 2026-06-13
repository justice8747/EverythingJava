import java.util.Scanner;

public class userInput {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("what is todays date");

        String date = sc.nextLine();

        System.out.println("todays date is " + date);

        /* just like data types we can accept different type of input and if you try to 
         * the wrong type you will get an error
        */

        System.out.println("How old are you");

        int age = sc.nextInt();
        sc.nextLine(); // consume newline left-over

            System.out.println(" you are " + age + " years old");

    }
}

