import java.util.concurrent.*;

public class ThreadPoo {

    public static void main(String[] args) {

        ThreadPoolExecutor tpe = new ThreadPoolExecutor(1,200,10, TimeUnit.SECONDS,new LinkedBlockingQueue<>(5));

        for (int i = 0; i <1000 ; i++) {
            int finalI = i;
            //System.out.println("添加新任务"+i);
            try{
                tpe.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("启动"+ Thread.currentThread().getName() );
                        //System.out.println("队列头"+ tpe.getQueue().size());
                    }
                });
            }
            catch (Exception e){
                e.printStackTrace();
            }

            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        /*ThreadPoolExecutor tpe = new ThreadPoolExecutor(0,Integer.MAX_VALUE,0, TimeUnit.SECONDS,new SynchronousQueue<>());
        for (int i = 0; i <1000 ; i++) {
            int finalI = i;
            System.out.println("添加新任务" + i);
            tpe.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("启动" + Thread.currentThread().getName() );
                    //System.out.println("队列头" + tpe.getActiveCount());
                }
            });
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
    }
}
