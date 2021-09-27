package StreamTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StreamTest1 {

    public static void main(String[] args) {
        List<User> users = Arrays.asList(new User("wg",27),new User("wg1",272),new User("wg2",274),new User("wg3",323),new User("wg4",444));
        //筛选
        List<User> newList=users.stream().filter(e->{return e.getAge()>300;}).collect(Collectors.toList());
        newList.forEach(e-> System.out.println(e));


        List<Double> newList2=users.stream().mapToDouble(User::getAge).collect(
                    ArrayList::new,
                        (list2,value)->list2.add(value),
                (list1,list2)->list1.addAll(list2)
        );
        newList2.forEach(e-> System.out.println(e));
        //newList2=users.stream().mapToDouble(User::getAge).collect();
        //newList2.forEach(e-> System.out.println(e));

        List<Integer> newList3=users.stream().map(e->e.getAge()+e.getAge()).collect(Collectors.toList());
        newList3.forEach(e-> System.out.println(e));
        /* users.stream().filter(e-> {
                e.setAge(20);
                return true;
        }).collect(Collectors.toList());*/
        List a2 =Arrays.asList(1,2,3);
        System.out.println(a2.get(0).getClass());
        int[] a3={1,2,3};
        List a4 =Arrays.asList(a3);
        System.out.println(a4.get(0).getClass());
        System.out.println("----------newList4----------T reduce(T identity, BinaryOperator<T> accumulator);");

        Integer newList4=users.stream().map(e->e.getAge()).reduce(5,Integer::sum);
        System.out.println(newList4);
        System.out.println("----------newList6----------reduce(BinaryOperator<T> accumulator)");
        Integer newList6=users.stream().map(e->e.getAge()).reduce((a,b)->{
            System.out.println(a);
            System.out.println(b);
            System.out.println(a+b);
            return a+b;
        }).get();
        System.out.println("----------newList5----------");

        Integer newList5=users.stream().map(e->e.getAge()+e.getAge()).min(Integer::compareTo).get();

        System.out.println(newList5);
    }
}

class User{
    private String name;

    private int age;


    User(String name,int age){
        this.name=name;
        this.age=age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}