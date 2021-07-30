public class StringTest {

    public static void main(String[] args) {

        String a = "fuck";
        String b = new String("fuck");
        System.out.println(a.hashCode()+" "+b.hashCode());
        System.out.println(a.equals(b));
        System.out.println(a==b); //false
        System.out.println(System.identityHashCode(a)+ " "+System.identityHashCode(b));
        System.out.println("-------------------------");
        b=b.intern();

        System.out.println(a.hashCode()+" "+b.hashCode());
        System.out.println(a.equals(b));
        System.out.println(a==b); //false
        System.out.println(System.identityHashCode(a)+ " "+System.identityHashCode(b));
        System.out.println("----------String c = a+\"c\";\n" +
                "        String d = \"fuckc\";---------------");

        String c = a+"c";
        String d = "fuckc";
        System.out.println(c.hashCode()+" "+d.hashCode());
        System.out.println(c.equals(d));
        System.out.println(c==d); //false
        System.out.println(System.identityHashCode(c)+ " "+System.identityHashCode(d));
        System.out.println("----------c = \"fuck\"+\"d\";\n" +
                "        d = \"fuckd\";---------------");

        c = "fuck"+"d";
        d = "fuckd";
        System.out.println(c.hashCode()+" "+d.hashCode());
        System.out.println(c.equals(d));
        System.out.println(c==d); //false
        System.out.println(System.identityHashCode(c)+ " "+System.identityHashCode(d));
        System.out.println("-----------StringBuffer e =new StringBuffer(\"cao\");\n" +
                "        StringBuffer f =new StringBuffer(\"cao\");--------------");
        StringBuffer e =new StringBuffer("cao");
        StringBuffer f =new StringBuffer("cao");
        System.out.println(e.hashCode()+" "+f.hashCode());
        System.out.println(e.equals(f));
        System.out.println(e==f); //false
        System.out.println(System.identityHashCode(e)+ " "+System.identityHashCode(f));

    }
}
