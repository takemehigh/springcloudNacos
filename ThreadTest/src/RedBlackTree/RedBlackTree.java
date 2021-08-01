package RedBlackTree;

/**
 * @author wg
 */
public class RedBlackTree{

    private TreeNode root;

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }



    public TreeNode iterator(TreeNode node){
        if(node==null)return null;
        else{
            System.out.println(node);
            iterator(node.getLeft());
            iterator(node.getRight());
            return node;
        }


    }

    public TreeNode insert(TreeNode node){

        //如果root节点为空直接把插入节点设置成root
        //从根节点开始遍历节点，定位到要set的节点
        TreeNode currentNode = root;
        TreeNode parent = null;
        while(
                currentNode!=null
        ){
            parent = currentNode;
            if(node.getData().compareTo(currentNode.getData())<0){
                currentNode = currentNode.getLeft();
            }
            else{
                currentNode = currentNode.getRight();
            }
        }

        node.setParent(parent);
        node.setColor(Color.RED);

        if(parent!=null){
            if(node.getData().compareTo(parent.getData())<0){
                parent.setLeft(node);
            }
            else{
                parent.setRight(node);
            }
        }
        else{
            this.root = node;
        }

        node.setColor(Color.RED);

        insertFix(node);

        return node;
    }

    private void insertFix(TreeNode node) {

        //红黑树 几种情况 新加入节点 黑父
        //黑父 不影响黑高 不操作


        TreeNode newnode = node;
        TreeNode gparent,parent;


        while((parent=newnode.getParent())!=null&&parent.getColor()==Color.RED){
            //红父 情况1.红叔，父叔变色，祖节点变色，再将祖节点作为新节点进行迭代
            //分析红父 红叔 父叔 变黑 ,爷变黑,把新的顶替老parent的做为新的parent做迭代。
            gparent = parent.getParent();

            TreeNode uncle;
            //
            if(gparent.getLeft()==parent){
                uncle = gparent.getRight();
                if(uncle!=null&&uncle.getColor()==Color.RED){
                    uncle.setColor(Color.BLACK);
                    parent.setColor(Color.BLACK);
                    gparent.setColor(Color.RED);
                    newnode=gparent;
                    continue;
                }
                //叔 为黑的情况，当新节点在父节点的右边时，先将父节电左旋，形成左红父黑叔
                if(newnode==parent.getRight()){
                    leftRotate(parent);
                    //父节点和新节点互换
                    TreeNode tmp = parent;
                    parent = newnode;
                    newnode = tmp;
                }
                //左红父黑叔统一操作，祖节点右旋，然后父祖变色
                rightRotate(gparent);
                gparent.setColor(Color.RED);
                parent.setColor(Color.BLACK);

            }
            else{
                //跟上面全部反过来
                uncle = gparent.getLeft();
                if(uncle!=null&&uncle.getColor()==Color.RED){
                    uncle.setColor(Color.BLACK);
                    parent.setColor(Color.BLACK);
                    gparent.setColor(Color.RED);
                    newnode=gparent;
                    continue;
                }
                //叔 为黑的情况，当新节点在父节点的右边时，先将父节电左旋，形成左红父黑叔
                if(newnode==parent.getLeft()){
                    rightRotate(parent);
                    //父节点和新节点互换
                    TreeNode tmp = parent;
                    parent = newnode;
                    newnode = tmp;
                }
                //左红父黑叔统一操作，祖节点右旋，然后父祖变色
                leftRotate(gparent);
                gparent.setColor(Color.RED);
                parent.setColor(Color.BLACK);
            }


        }
        this.root.setColor(Color.BLACK);
    }

    private void leftRotate(TreeNode node) {
        TreeNode rightchild = node.getRight();
        TreeNode gparent = node.getParent();
        TreeNode newleft = rightchild.getLeft();
        if(gparent!=null){
            if(node.getParent().getLeft()==node)
            gparent.setLeft(rightchild);
            if(node.getParent().getRight()==node)
            gparent.setRight(rightchild);
            rightchild.setParent(gparent);
        }
        else{
            this.root = rightchild;
            rightchild.setParent(null);
        }
        node.setParent(rightchild);
        node.setRight(newleft);
        if(newleft!=null){
            newleft.setParent(node);
        }
        rightchild.setLeft(node);
    }

    private void rightRotate(TreeNode node) {
        TreeNode leftchild = node.getLeft();
        TreeNode gparent = node.getParent();
        TreeNode newright = leftchild.getRight();

        if(gparent!=null){
            if(node.getParent().getLeft()==node)
                gparent.setLeft(leftchild);
            if(node.getParent().getRight()==node)
                gparent.setRight(leftchild);

            leftchild.setParent(gparent);

        }
        else{
            this.root = leftchild;
            leftchild.setParent(null);

        }
        node.setParent(leftchild);
        node.setLeft(newright);
        if(newright!=null){
            newright.setParent(node);
        }
        leftchild.setRight(node);
    }
}


class TreeNode{

    private Comparable data;

    private TreeNode left;

    private TreeNode right;

    public TreeNode(Comparable data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "TreeNode{" +
                "data=" + data +

                ", color=" + color +
                '}';
    }

    private TreeNode parent;

    private Color color;

    public Comparable getData() {
        return data;
    }

    public void setData(Comparable data) {
        this.data = data;
    }

    public TreeNode getLeft() {
        return left;
    }

    public void setLeft(TreeNode left) {
        this.left = left;
    }

    public TreeNode getRight() {
        return right;
    }

    public void setRight(TreeNode right) {
        this.right = right;
    }

    public TreeNode getParent() {
        return parent;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}

enum Color{

    RED,
    BLACK;

}