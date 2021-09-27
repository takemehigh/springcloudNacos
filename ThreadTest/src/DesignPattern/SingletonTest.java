package DesignPattern;

public class SingletonTest {

    public static void main(String[] args) {
        Singleton lazy=Singleton.getInstanceLazy();
        Singleton hungry=Singleton.getInstanceHungry();
        System.out.println(lazy);
        System.out.println(hungry);
        lazy=Singleton.getInstanceLazy();
        hungry=Singleton.getInstanceHungry();
        System.out.println(lazy);
        System.out.println(hungry);

        System.out.println(OuterSingleTon.getInstance());
    }

}

class Singleton{

    private static Singleton lazyobject ;
    private volatile static  Singleton hungryobject = new Singleton();
    private Singleton(){

    }

    public static Singleton getInstanceLazy(){
        if(lazyobject == null){
            synchronized (Singleton.class){
                if(lazyobject==null){
                    lazyobject = new Singleton();
                }
            }
            lazyobject = new Singleton();
        }
        return lazyobject;
    }

    public static Singleton getInstanceHungry(){
        return hungryobject;
    }

}

class OuterSingleTon{

    private static class InnerHold{
        private static OuterSingleTon outerSingleTon = new OuterSingleTon();
    }

    private OuterSingleTon(){}

    public static OuterSingleTon getInstance(){
        return InnerHold.outerSingleTon;
    }

}