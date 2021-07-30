package RedBlackTree;

public class RedBlackTree{

    private TreeNode<Comparable> root;

    public TreeNode<Comparable> insert(TreeNode<Comparable> node){

        //如果root节点为空直接把插入节点设置成root
        //从根节点开始遍历节点，定位到要set的节点
        TreeNode<Comparable> currentNode = root;
        TreeNode<Comparable> parent = null;
        while(
                (root!=null||(root=node)!=null)&&currentNode!=null
        ){
            if(node.getData().compareTo(currentNode.getData())<0){
                parent = currentNode;
                currentNode = currentNode.getLeft();
            }
            if(node.getData().compareTo(currentNode.getData())<0){
                parent = currentNode;
                currentNode = currentNode.getRight();
            }
        }

        node.setParent(parent);

        if(node.getData().compareTo(parent.getData())<0){
            parent.setLeft(node);
        }
        if(node.getData().compareTo(parent.getData())<0){
            parent.setRight(node);
        }
        node.setColor(Color.RED);
        
        insertFix(node);

        return node;
    }

    private void insertFix(TreeNode<Comparable> node) {

        //红黑树 几种情况 新加入节点 黑父
        //黑父 不影响黑高 不操作
        //红父

        TreeNode<Comparable> newnode = node;
        TreeNode<Comparable> gparent,parent;
        while((parent=newnode.getParent())!=null&&parent.getColor()==Color.RED){
            //情况1：红叔  父叔变黑，祖节点变红，向上迭代到根节点
            gparent = parent.getParent();
            TreeNode<Comparable> uncle;
            //
            if(parent.getLeft()==node){
                uncle = parent.getRight();
            }
            else{
                uncle = parent.getLeft();
            }
            
        }

    }


}


class TreeNode<T>{

    private T data;

    private TreeNode<T> left;

    private TreeNode<T> right;

    private TreeNode<T> parent;

    private Color color;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public TreeNode<T> getLeft() {
        return left;
    }

    public void setLeft(TreeNode<T> left) {
        this.left = left;
    }

    public TreeNode<T> getRight() {
        return right;
    }

    public void setRight(TreeNode<T> right) {
        this.right = right;
    }

    public TreeNode<T> getParent() {
        return parent;
    }

    public void setParent(TreeNode<T> parent) {
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