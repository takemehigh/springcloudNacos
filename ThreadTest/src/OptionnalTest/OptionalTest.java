package OptionnalTest;

import java.util.Optional;

/**
 * @author: wg
 * @date: 2021/8/11 17:07
 * @describe:
 * @version:
 */
public class OptionalTest {

    public static void main(String[] args) {
        Optional optional = Optional.ofNullable(null);
        System.out.println(optional);
    }
}
