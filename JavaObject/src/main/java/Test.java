import org.openjdk.jol.info.ClassLayout;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Test {

    public static void main(String[] args) {
        String a="ASD";
        byte[] bs=a.getBytes(StandardCharsets.UTF_8);
        for(Byte b : bs){
            System.out.println(b.toString());
        }
        System.out.println(Arrays.toString(bs));

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
