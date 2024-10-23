package loop;



public class loop {
    public static void main(String[] args) {
        int num = 1;

         //while loop
        while(num < 10){
            System.out.println(num);
            num++;
        }

         //for long 
        for(int i = 1; i < 20; i++){
            System.out.println(i);
    }

    //doWhile loop

     do{
        System.out.println(num);
        num++;
     }while(num < 10);

     //nested loop 
     for(int i = 1; i < 5; i++){
        for(int j = 1; j < 5; j++){
            int result = i * j;
            System.out.println(result);
        }
     }
}
}
