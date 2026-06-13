import java.util.Scanner;

public class stringMethods {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("what is you mane");

        String name = sc.nextLine();

        //the equal method
        boolean result = name.equals("Justice");

        //you can also ignore case sensitivity
        boolean rst = name.equalsIgnoreCase("Justice");

        //string lemght method
        int length = name.length();

        //return a single character method
        char firstChar = name.charAt(0);


        //finding a character and return its index
        int index = name.indexOf("u");

        //check if a string is empty
        boolean isEmpty = name.isEmpty();

        //upper and lower case
        String upperCase = name.toUpperCase();
        String lowerCase = name.toLowerCase();

        //removing an empty space before and after a string
        String trimmed = name.trim();

        //replacing a character in a string
        String replace = name.replace("u", "o");


        System.out.println(replace);
        System.out.println(trimmed);

        
    }
}
