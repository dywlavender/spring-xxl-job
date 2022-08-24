package baidu.com;

/**
 * @author dyw
 * @date 2022-08-24  21:25
 */
public class BinaryTreeDemo {
    public static void main(String[] args) {
        BinaryTree binaryTree = new BinaryTree();
        HeroNode root = new HeroNode(1, "1");
        HeroNode node2 = new HeroNode(2, "2");
        HeroNode node3 = new HeroNode(3, "3");
        HeroNode node4 = new HeroNode(4, "4");
        HeroNode node5 = new HeroNode(5, "5");
        root.setLeft(node2);
        root.setRight(node3);
        node3.setRight(node4);
        node3.setLeft(node5);
        binaryTree.setRoot(root);
        System.out.println("per");
        binaryTree.perOrder();
        System.out.println("infix");
        binaryTree.infixOrder();
        System.out.println("post");
        binaryTree.postOrder();
        HeroNode heroNode = null;
        System.out.println("per search");
        heroNode = binaryTree.perOrderSearch(6);
        System.out.println(heroNode);
        System.out.println("infix search");
        heroNode = binaryTree.infixOrderSearch(5);
        System.out.println(heroNode);
        System.out.println("post search");
        heroNode = binaryTree.postOrderSearch(3);
        System.out.println(heroNode);

    }


}

class BinaryTree{
    private HeroNode root;

    public void setRoot(HeroNode root) {
        this.root = root;
    }

    public void perOrder(){
        if (this.root != null){
            this.root.perOrder();
        }else {
            System.out.println("empty");
        }
    }
    public void infixOrder(){
        if (this.root != null){
            this.root.infixOrder();
        }else {
            System.out.println("empty");
        }
    }
    public void postOrder(){
        if (this.root != null){
            this.root.postOrder();
        }else {
            System.out.println("empty");
        }
    }

    public HeroNode perOrderSearch(int no){
        if (this.root != null){
            return this.root.perOrderSearch(no);
        }else {
            System.out.println("empty");
        }
        return null;
    }
    public HeroNode infixOrderSearch(int no){
        if (this.root != null){
            return this.root.infixOrderSearch(no);
        }else {
            System.out.println("empty");
        }
        return null;
    }
    public HeroNode postOrderSearch(int no){
        if (this.root != null){
            return this.root.postOrderSearch(no);
        }else {
            System.out.println("empty");
        }
        return null;
    }
}

class HeroNode {
    private int no;
    private String name;
    private HeroNode left;
    private HeroNode right;

    public HeroNode(int no, String name) {
        this.no = no;
        this.name = name;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HeroNode getLeft() {
        return left;
    }

    public void setLeft(HeroNode left) {
        this.left = left;
    }

    public HeroNode getRight() {
        return right;
    }

    public void setRight(HeroNode right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return "HeroNode{" +
                "no=" + no +
                ", name='" + name + '\'' +
                '}';
    }

    public void perOrder(){
        System.out.println(this);
        if (this.left != null){
            this.left.perOrder();
        }
        if (this.right != null){
            this.right.perOrder();
        }
    }

    public void infixOrder(){
        if (this.left != null){
            this.left.infixOrder();
        }
        System.out.println(this);
        if (this.right != null){
            this.right.infixOrder();
        }
    }

    public void postOrder(){
        if (this.left != null){
            this.left.postOrder();
        }
        if (this.right != null){
            this.right.postOrder();
        }
        System.out.println(this);
    }

    public HeroNode perOrderSearch(int no){
        if (this.no==no){
            return this;
        }
        HeroNode resNode = null;
        if (this.left!=null){
            resNode = this.left.perOrderSearch(no);
        }
        if (resNode != null){
            return resNode;
        }
        if (this.right != null){
            resNode = this.right.perOrderSearch(no);
        }
        return resNode;
    }

    public HeroNode infixOrderSearch(int no){

        HeroNode resNode = null;
        if (this.left!=null){
            resNode = this.left.perOrderSearch(no);
        }

        if (resNode != null){
            return resNode;
        }
        if (this.no==no){
            return this;
        }
        if (this.right != null){
            resNode = this.right.perOrderSearch(no);
        }
        return resNode;
    }

    public HeroNode postOrderSearch(int no){

        HeroNode resNode = null;
        if (this.left!=null){
            resNode = this.left.perOrderSearch(no);
        }
        if (resNode != null){
            return resNode;
        }
        if (this.right != null){
            resNode = this.right.perOrderSearch(no);
        }
        if (this.no==no){
            return this;
        }
        return resNode;
    }
}
