package concurrency;

public class thread3 {
     static class C extends Thread{
         public void run(){
             for(int i=0; i<50;i++){
                 System.out.println("class C printing");
                 try{
                     Thread.sleep(10);
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
             }
         }
     }
}
