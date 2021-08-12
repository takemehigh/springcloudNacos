package LambdaTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.*;

/**
 * @author: wg
 * @date: 2021/8/10 10:14
 * @describe:
 * @version:
 */
public class LambaTest1 {

    public static void main(String[] args) {
        String a= "a!a!a";
        String[] b=a.split("!");
        System.out.println(Arrays.deepToString(b));

        Arrays.asList( "a", "b", "d" ).forEach(e -> System.out.println( e ) );
        Arrays.asList( "a", "b", "d" ).forEach((String e) -> System.out.println( e ) );

        Arrays.asList( "a", "b", "d" ).sort(LambaTest1::compare1);

        List names = Arrays.asList("peter", "anna", "mike", "xenia");

        Collections.sort(names, Comparator.comparing((String a2) -> a2.substring(1, 2)));
        System.out.println(names.get(0));

        MyInfterFace mif = (e1)->{
            System.out.println(e1);
        };

        mif.print("cao");

        LambaTest1 test1=new LambaTest1();
        test1.testThread();
        //接受一个参数
        Function<String,Integer> f = Integer::parseInt;
        int res=f.apply("1");
        //接受两个参数BIGFUNCTION
        BiFunction<String,Integer,Integer> g = Integer::parseInt;
        int res2 = g.apply("1",10);
        System.out.println(res+res2);
        TestMethodReference testMethodReference =new TestMethodReference();
        //接受一个参数无返回
        Consumer<String> function=testMethodReference::test1;
        //接受2个参数无返回
        BiConsumer<String,String> biConsumer = testMethodReference::test1;
        biConsumer.accept("asdasdsa","ddddddddd");
        function.accept("wg");
        //无参数 有返回
        Supplier<String> supplier = testMethodReference::test1;
        System.out.println(supplier.get());
    }

    void testThread(){
        new Thread(()->{
            System.out.println(this);
        }).start();
    }

    private static int compare1(String e1, String e2) {
        int result = e1.compareTo(e2);
        return result;
    }

    private static String compare2( String e2) {
        return e2;
    }


}
class TestMethodReference{

    void test1(String a, String b){
        System.out.println(a+b);
    }

    void test1(String a){
        System.out.println(a);
    }
    String test1(){
        return "a";
    }
}

@FunctionalInterface
interface MyInfterFace<T>{

    public default void testDefault(){
        System.out.println("father");
    };

    void print(T t);
}

class MyInfterFace2<T> implements  MyInfterFace{

    public void testDefault(){};

    @Override
    public void print(Object o) {

    }
}