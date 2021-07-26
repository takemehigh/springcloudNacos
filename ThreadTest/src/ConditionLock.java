import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionLock {

    static final Lock lock = new ReentrantLock();

    static final Condition notfull = lock.newCondition();
    static final Condition notEmpty = lock.newCondition();

    final static List items = new ArrayList();
    static volatile int count = 0;
    static int b=0;

    public static void main(String[] args) throws InterruptedException {
        System.out.println(b++);
        System.out.println(++b);
        List<Thread> l = new ArrayList();
        CountDownLatch threadSignal = new CountDownLatch(2);
        for (int i = 0; i < 1; i++) {
            Thread thread1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (count<50000){
                        lock.lock();
                        try {
                            while (items.size()>10){
                                System.out.println("太多"+items.size());

                                notEmpty.await();
                                System.out.println("生产1被唤醒 ");

                            }
                            Object a = new Object();
                            items.add(a);


                            notfull.signal();
                            //notfull.signalAll();

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        finally {

                            lock.unlock();
                            System.out.println("生产1 "+(count++));
                        }
                    }
                    threadSignal.countDown();

                }


            });
            l.add(thread1);
        }




        for (int i = 0; i < 1; i++) {
            Thread thread2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (count<50000){
                        lock.lock();
                        try {
                            while (items.size()==0){
                                System.out.println("没了"+items.size());

                                notfull.await();
                                System.out.println("消费1被唤醒 ");

                            }
                            items.remove(0);

                            //notEmpty.signalAll();
                            notEmpty.signal();

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        finally {
                            lock.unlock();
                            System.out.println("消费1 "+(count++));

                        }
                    }
                    threadSignal.countDown();

                }

            });
            l.add(thread2);

        }

        for (Thread i:l
             ) {

            i.start();
        }
        System.out.println("倒计时");
        threadSignal.await();
        System.out.println(items.size());
        System.out.println(l.size());

        System.out.println("结束");
    }

}
