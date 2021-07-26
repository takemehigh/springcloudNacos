public class FinalTest {

    int i;

    final int j;

    static public FinalTest finalTest;

    FinalTest(){
        this.i=1;
        this.j=2;
    }

    public static void main(String[] args) {
        int i = 2;int j = i;i++;i = i + 1;        System.out.println(i);


        System.out.println(j);

        Thread t1= new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finalTest = new FinalTest();
            }
        });

        Thread t2= new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(finalTest);
                System.out.println(finalTest.i);
                System.out.println(finalTest.j);

            }
        });
        t1.start();
        t2.start();
    }
}
