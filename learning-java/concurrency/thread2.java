package concurrency;

public class thread2 {
    static class B extends Thread{
        public void run(){
            for(int i=0; i<50;i++){
                System.out.println("class B printing");
                try{
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
