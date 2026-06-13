package practices;

import java.util.ArrayList;
import java.util.Scanner;

public class arithmetic_sequence {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the first term of the sequence: ");
        int a = sc.nextInt();
        sc.nextLine();
        System.out.println("Enter the common difference of the sequence: ");
        int d = sc.nextInt();
        sc.nextLine();
        System.out.println("Enter the number of terms you want in the sequence ");
        // number of terms in the sequence
         int n = sc.nextInt();
        sc.nextLine();

        System.out.println(fun(a, d, n));
        sc.close();
    }

    static int fun(int a, int d, int n){
         ArrayList<Integer> apList = new ArrayList<>();
         
         int result= a;
         int num = a;
         apList.add(a);
         while(apList.size() < n){
            apList.add(num + d);
            num += d;
            result += num;
         }
         System.out.println(apList);
        return result; 
    };

}
