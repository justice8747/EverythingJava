package practices;

import java.util.Scanner;

public class sum_of_digits {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter digits more than two to sum them");
        int n = sc.nextInt();

        System.out.println(sumOfDigits(n));

    }

    static int sumOfDigits(int num){
        //fisrt turn the number to a string
        String str = Integer.toString(num);

        //then convert the string to array of character
        char[] charArray = str.toCharArray();
        int result = 0;
        for(char i: charArray){
            //then convert each char in the array to str then to int then add
          String str1 = String.valueOf(i);
            result += Integer.parseInt(str1);
        }
        return result;
    };
}
