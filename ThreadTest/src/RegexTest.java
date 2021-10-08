import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wangge
 * @version 1.0
 * @date 2021/10/7 3:04
 */
public class RegexTest {
    public static void main(String[] args) {
        //String a="<a href=\"www.baidu.com\">百度一下</a>";
        String a="${name}-${age}-wg-${gender}-${{gender}}";

        //String patternStr="((?<=(<)).*(?<=(>)))";
        String patternStr="^\\$\\{([^{}]+?)\\}$";
        String patternStr2="\\$\\{[^{}]+?\\}";
        String patternStr3="\\$\\{\\}";
        String patternStr4="\\$\\{([^{}]+?)\\}";

        Pattern pattern0=Pattern.compile(patternStr);
        Matcher matcher0=pattern0.matcher(a);
        System.out.println(matcher0.groupCount());
        while(matcher0.find()){
            int i=matcher0.groupCount();
            for (int j = 0; j <= i; j++) {
                System.out.println(i+"-----"+matcher0.group(j)+"-----"+matcher0.start(j)+"-----"+matcher0.end(j));
            }

            System.out.println("------------------------");

        }

        //(?<=(href=")).{1,200}(?=(">))
        String s="<br/>您好，非常好，很开心认识你\r\n" +
                "<br/><a target=_blank href=\"www.baidu.com\">百度一下</a>百度才知道\r\n" +
                "<br/><a target=_blank href=\"/view/fafa.htm\">发发</ a>最佳帅哥\r\n" +
                "<br/><a target=_blank href=\"/view/lili.htm\">丽丽</ a>最佳美女\r\n" +
                "<br/>";
        String pattern="(?<=(href=\")).*(?=(\">))";
        Pattern p=Pattern.compile(pattern);
        //Pattern p=Pattern.compile(pattern1);
        Matcher m=p.matcher(s);
        while(m.find()) {
            //System.out.println(m);
            System.out.println(m.group(0));
            System.out.println(m.group(1));
            System.out.println(m.group(2));
            System.out.println("------------");
        }
        /*String s1="dog";
        String pattern1="((.)(.(.)))";
        Pattern p1=Pattern.compile(pattern1);
        Matcher m1=p1.matcher(s1);
        while(m1.find()) {
            //System.out.println(m);
            System.out.println(m1.group(0));
            System.out.println(m1.group(1));
            System.out.println(m1.group(2));
            System.out.println(m1.group(3));
            System.out.println(m1.group(4));
        }*/

    }
}
