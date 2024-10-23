
import java.util.Scanner;
public class if_statement {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("How old are you");
        int age = sc.nextInt();
        sc.nextLine();

    if(age > 18 && age <50){
        System.out.println("you are an adult");
    }else if(age <= 18 && age >= 12){
        System.out.println("you are still a teenager");
    }else if(age < 12){
        System.out.println("you are still a kid");
    }else if(age > 50 || age > 60){
        System.out.println("you are an old man");
    }
    }

}


