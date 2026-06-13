import java.util.Scanner;

public class switcStatement {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("What day is today: " );
        String day = sc.nextLine();

        switch (day) {
            case "monday":
                System.out.println("today is monday");
                break;
            case "tuesday":
                System.out.println("today is tuesday");
                break;
            case "wednesday":
                System.out.println("today is wedesdays");
                break;
            case "thursday":
                System.out.println("today is thursday");
                break;
            case "friday":
                System.out.println("today is friday");
                break;
            case "sartuday":
                System.out.println("today is sartuday");
                break;
            case "sunday":
                System.out.println("today is sunday");
                break;
            
            default:
                System.out.println(day);
        }
          sc.close();
    }
}
