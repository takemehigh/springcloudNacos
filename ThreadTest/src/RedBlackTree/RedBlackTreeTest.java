package RedBlackTree;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: wg
 * @date: 2021/7/30 13:53
 * @describe:
 * @version:
 */
public class RedBlackTreeTest {
    public static void main(String[] args) {

        /*List<? super List> list =new ArrayList();
        list.add(new ArrayList());

        TreeNode T1= new TreeNode();
        TreeNode T2 = T1;
        System.out.println(T1);

        System.out.println(T2);
        T1 = new TreeNode();
        System.out.println(T1);

        System.out.println(T2);*/
        RedBlackTree rbt = new RedBlackTree();
        for (int i = 0; i < 100; i++) {
            rbt.insert(new TreeNode(i));
        }
        rbt.iterator(rbt.getRoot());
    }
}
