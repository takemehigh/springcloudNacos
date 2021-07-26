import org.openjdk.jol.info.ClassLayout;

public class Test {

    public static void main(String[] args) {
        User u = new User();
        u.setAge(1);
        u.setName("wg324324ff");
        User[] ua = new User[10];
        System.out.println(ClassLayout.parseInstance(u).instanceSize());
        System.out.println(ClassLayout.parseInstance(u).toPrintable());
        System.out.println(ClassLayout.parseInstance(ua).instanceSize());
        System.out.println(ClassLayout.parseInstance(ua).toPrintable());
    }
}
