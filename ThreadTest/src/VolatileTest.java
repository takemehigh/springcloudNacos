import java.util.Vector;

public class VolatileTest extends Thread{

    private static volatile int count = 0;
    public Object lock = new Object();
    public  Object lock2 ;

    public VolatileTest(Object lock2) {
        this.lock2 = lock2;
    }

    public VolatileTest(){

    }
    public static void main(String[] args) throws Exception {

        //由于多线程情况下未必会试出重排序的结论,所以多试一些次
        for(int i=0;i<1000;i++){
            ThreadA threadA=new ThreadA();
            ThreadB threadB=new ThreadB();
            threadA.start();
            threadB.start();

            //这里等待线程结束后,重置共享变量,以使验证结果的工作变得简单些.
            threadA.join();
            threadB.join();
            a=0;
            flag=false;
        }


        /*Vector<Thread> threads = new Vector<>();
        VolatileTest volatileTest=new VolatileTest();

        for (int i = 0; i < 100; i++) {
            VolatileTest2 thread = volatileTest.new VolatileTest2();
            threads.add(thread);
            thread.start();
        }
        for (int i = 0; i < 100; i++) {
            VolatileTest1 thread = volatileTest.new VolatileTest1();
            threads.add(thread);
            thread.start();
        }
        //等待子线程全部完成
        for (Thread thread : threads) {
            thread.join();
        }
        //输出结果，正确结果应该是1000，实际却是984
        System.out.println(count);//984*/

    }


    class VolatileTest2 extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    //休眠500毫秒
                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(count);
            }
        }
    }

    class VolatileTest1 extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    //休眠500毫秒
                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                synchronized (lock){
                    count++;
                }
            }
        }
    }
    /** 这是一个验证结果的变量 */
    private static int a=0;
    /** 这是一个标志位 */
    private static boolean flag=false;
    static class ThreadA extends Thread{
        public void run(){
            a=1;
            flag=true;
        }
    }

    static class ThreadB extends Thread{
        public void run(){
            if(flag){
                a=a*1;
            }
            if(a==0){
                System.out.println("ha,a==0");
            }
        }
    }

}
