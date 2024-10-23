package concurrency;

public class thread1 {
    /*thread in simply english is the ability the break down your software into smaller pieces so they can be
    * able to run at the same time.*/
    static class A extends Thread{
        public void run(){
            for(int i=0; i<50;i++){
                System.out.println("class A printing");
                try{
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                   e.printStackTrace();
                }

            }

        }
    }
}
