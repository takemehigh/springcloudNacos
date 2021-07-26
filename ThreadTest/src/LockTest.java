import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;

public class LockTest {
    //公平锁
    static ReentrantLock lock1 = new ReentrantLock(true);

    //不公平锁
    static ReentrantLock lock2 = new ReentrantLock(false) ;
    private static volatile int count = 0;
    volatile transient LockTest lockTest;
    volatile String a;
    volatile String b;
    public final boolean hasQueuedPredecessors() {
        // The correctness of this depends on head being initialized
        // before tail and on head.next being accurate if the current
        // thread is first in queue.
        String t = a; // Read fields in reverse initialization order
        String h = b;
        String s;
        return h != t &&
                ((s = h.toString()) == null );
    }

    public static void main(String[] args) throws Exception {

        LockTest b=new LockTest();
        String t = b.a; // Read fields in reverse initialization order
        String h = b.b;
        String s;
        System.out.println(h != t &&
                ((s = h.toString()) == null ));
        System.out.println(b.hasQueuedPredecessors());

        LockTest d=new LockTest();
        LockTest c=d.lockTest;

        LockTest s1;
        //System.out.println(d!=c);
        System.out.println(d==c&&(s1 = c.lockTest) == null);


        Vector<Thread> threads = new Vector<>();
        for (int i = 0; i < 100; i++) {
            VolatileTest thread = new VolatileTest(lock2);
            threads.add(thread);
            thread.start();
        }
        //等待子线程全部完成
        for (Thread thread : threads) {
            thread.join();
        }
        //输出结果，正确结果应该是1000，实际却是984
        System.out.println(count);//984

        lock1.lock();

    }


}
