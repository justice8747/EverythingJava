package concurrency;

public class multithreading {
    public static void main(String[] args) {
//        here let try to instantiate the thread classes
        thread1.A a = new thread1.A();
        thread2.B b = new thread2.B();
        thread3.C c = new thread3.C();

//        a.print();
//        b.print();
//        c.print();

//        but we want to be able to use thread to call all the thread class so they can run at the sam time
//        her is how

        a.start();
        b.start();
        c.start();

//        but first, we should have a run method in the classes

//        we can go on and optimise it so they classes can run simultaneously
//        to do this we can make a suggestion to the our scheduler to prioritise a class over another

        b.setPriority(Thread.MIN_PRIORITY+4);
        a.setPriority(Thread.MAX_PRIORITY-3);
        c.setPriority(Thread.MAX_PRIORITY-5);

//        but we can only suggest we can not entire control they scheduler
//        but there is another eay to do it
        /*to be able to control how the classes run or to be able to make sure the class
        * run in sequence, we can set time to the classes. like set time to each class so that
        * the can run and wait for some while so that the other classes cn run and also wait*/
    }
}
