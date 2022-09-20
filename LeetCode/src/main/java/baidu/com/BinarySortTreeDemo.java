package baidu.com;

/**
 * @ClassName BinarySortTree
 * @Description TODO
 * @Author dlavender
 * @Date 2022/9/18 18:47
 * @Version 1.0
 **/
public class BinarySortTreeDemo {
    public static void main(String[] args) {
        // int[] arr = {7,3,10,12,5,1,9};
        int[] arr = {1,2,3,4,5,6,7};
        BinarySortTree binarySortTree = new BinarySortTree();
        for (int i = 0; i < arr.length; i++) {
            binarySortTree.add(new Node2(arr[i]));
        }
        binarySortTree.infixOrder();
        System.out.println("------------");
        System.out.println(binarySortTree.getRoot().heigth());
        System.out.println(binarySortTree.getRoot().leftHeigth());
        System.out.println(binarySortTree.getRoot().rightHeight());
        System.out.println("----------");
        binarySortTree.delNode(5);
        binarySortTree.infixOrder();
        System.out.println(binarySortTree.getRoot().heigth());
        System.out.println(binarySortTree.getRoot().leftHeigth());
        System.out.println(binarySortTree.getRoot().rightHeight());
        System.out.println("----------");
    }
}

class BinarySortTree{
    private Node2 root;

    public Node2 getRoot(){
        return root;
    }

    public void add(Node2 node){
        if (root == null){
            root = node;
        }else {
            root.add(node);
        }

    }

    public void infixOrder(){
        if (root != null){
            root.infixOrder();
        }
    }

    public Node2 search(int value){
        if (root == null){
            return null;
        }else{
            return root.search(value);
        }

    }
    public Node2 searchParent(int value){
        if (root == null){
            return null;
        }else{
            return root.searchParent(value);
        }
    }

    public void delNode(int value){
        if (root != null){
            Node2 targetNode = search(value);
            if (targetNode == null){
                return;
            }
            if (root.left == null && root.right == null) {
                root = null;
                return;
            }

            Node2 parent = searchParent(value);
            if (targetNode.left == null && targetNode.right == null){
                if (parent.left != null && parent.left.value == value){
                    parent.left = null;
                }else if (parent.right != null && parent.right.value == value){
                    parent.right = null;
                }
            }else if (targetNode.left != null && targetNode.right != null){
                targetNode.value = delRigthTreeMin(targetNode.right);

            }else {
                if (targetNode.left != null){
                    if (parent.left.value == value){
                        parent.left = targetNode.left;
                    }else {
                        parent.right = targetNode.left;
                    }
                }else {
                    if (parent.left.value == value){
                        parent.left = targetNode.right;
                    }else {
                        parent.right = targetNode.right;
                    }
                }
            }
        }
    }
    public int delRigthTreeMin(Node2 node){
        Node2 target = node;
        while(target.left != null){
            target = target.left;
        }
        delNode(target.value);
        return target.value;
    }

    public int delLeftTreeMin(Node2 node){
        Node2 target = node;
        while(target.right != null){
            target = target.right;
        }
        delNode(target.value);
        return target.value;
    }
}

class Node2{
    int value;
    Node2 left;
    Node2 right;

    public Node2(int value){
        this.value = value;
    }

    public void add(Node2 node){
        if (node == null){
            return;
        }
        if (node.value < this.value){
            if (this.left == null){
                this.left = node;
            }else{
                this.left.add(node);
            }
        }else {
            if (this.right == null){
                this.right = node;
            }else {
                this.right.add(node);
            }
        }

        if (rightHeight()-leftHeigth()>1){
            if (right.leftHeigth()<right.leftHeigth()){
                right.rightRotate();
                leftRotate();
            }else {
                leftRotate();
            }
            return;
        }
        if (leftHeigth() - rightHeight() > 1){
            if (left.rightHeight()>left.leftHeigth()){
                left.leftRotate();
                rightRotate();
            }else {
                leftRotate();
            }
        }

    }

    public void infixOrder() {
        if (this.left != null){
            this.left.infixOrder();
        }
        System.out.println(this.value);
        if (this.right != null){
            this.right.infixOrder();
        }
    }

    public Node2 search(int vaule){
        if (this.value == vaule){
            return this;
        }else if (vaule<this.value && this.left != null){
            return this.left.search(vaule);
        } else if (vaule>=this.value && this.right != null) {
            return this.right.search(vaule);

        }else {
            return null;
        }

    }
    public Node2 searchParent(int value){
        if ((this.left != null && this.left.value==value) ||
                (this.right != null && this.right.value==value)){
            return  this;
        }else {
            if (value < this.value && this.left!= null){
                return this.left.searchParent(value);
            }else if (value >= this.value && this.right !=null) {
                return this.right.searchParent(value);
            }else {
                return null;
            }
        }
    }

    private void leftRotate(){
        Node2 newNode = new Node2(value);
        newNode.left = this.left;
        newNode.right = this.right.left;
        this.value = this.right.value;
        this.left = newNode;
        this.right = this.right.right;
    }
    private void rightRotate(){
        Node2 newNode = new Node2(value);
        newNode.left = this.left.right;
        newNode.right = this.right;
        this.value = this.left.value;
        this.right = newNode;
        this.left = this.left.left;
    }
    public int heigth(){
        return Math.max(this.left==null ? 0 :this.left.heigth(),this.right==null? 0:this.right.heigth())+1;
    }
    public int rightHeight(){
        if (right == null){
            return 0;
        }
        return right.heigth();
    }
    public int leftHeigth(){
        if (left == null){
            return 0;
        }
        return left.heigth();
    }
}