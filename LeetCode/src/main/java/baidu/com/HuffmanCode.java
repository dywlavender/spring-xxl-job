package baidu.com;

import java.io.*;
import java.util.*;

/**
 * @ClassName hefumanTree
 * @Description TODO
 * @Author dlavender
 * @Date 2022/9/15 21:11
 * @Version 1.0
 **/
public class HuffmanCode {
    public static void main(String[] args) {
        String content = "i like like like java do you like a java";
        byte[] contentByte = content.getBytes();
        System.out.println(contentByte.length);
        byte[] huffmanCodeBytes = huffmanZip(contentByte);
        System.out.println(Arrays.toString(huffmanCodeBytes));

    }

    private static byte[] huffmanZip(byte[] contentByte){
        List<Node> nodes = getNodes(contentByte);
        System.out.println(nodes);

        Node huffmanTree = createHuffmanTree(nodes);
        huffmanTree.preOrder();
        // System.out.println(huffmanTree);
        Map<Byte, String> codes = getCodes(huffmanTree);
        System.out.println(codes);
        byte[] huffmanCodeBytes = zip(contentByte,codes);
        return huffmanCodeBytes;
    }

    private static byte[] zip(byte[] bytes,Map<Byte,String > huffmanCodes){
        StringBuilder stringBuilder1 = new StringBuilder();
        for (Byte b:bytes){
            stringBuilder1.append(huffmanCodes.get(b));
        }

        int len = (stringBuilder1.length()+7)/8;
        byte[] huffmanCodeBytes = new byte[len];
        int index = 0;
        for (int i = 0; i < stringBuilder1.length(); i+=8) {
            String strByte;
            if (i+8>stringBuilder1.length()){
                strByte = stringBuilder1.substring(i);
            }else {
                strByte = stringBuilder1.substring(i,i+8);
            }
            huffmanCodeBytes[index]=(byte) Integer.parseInt(strByte,2);
            index++;
        }
        return huffmanCodeBytes;
    }

    static Map<Byte,String> huffmanCodes = new HashMap<>();
    static StringBuilder stringBuilder = new StringBuilder();

    private static Map<Byte,String> getCodes(Node root){
        if (root == null){
            return null;
        }
        getCodes(root.left,"0",stringBuilder);
        getCodes(root.right,"1",stringBuilder);
        return huffmanCodes;
    }
    private static void getCodes(Node node, String code, StringBuilder stringBuilder){
        StringBuilder stringBuilder1 = new StringBuilder(stringBuilder);
        stringBuilder1.append(code);
        if (node!=null){
            if (node.data==null){
                getCodes(node.left,"0",stringBuilder1);
                getCodes(node.right,"1",stringBuilder1);
            }else {
                huffmanCodes.put(node.data,stringBuilder1.toString());
            }
        }
    }
    private static void preOrder(Node root){
        if (root!=null){
            root.preOrder();
        }else {
            System.out.println("empty");
        }
    }
    private static List<Node> getNodes(byte[] bytes){
        ArrayList<Node> nodes = new ArrayList<>();
        HashMap<Byte, Integer> counts = new HashMap<>();
        for (byte b : bytes){
            Integer count = counts.getOrDefault(b, 0) +1;
            counts.put(b,count);

        }

        for (Map.Entry<Byte,Integer> entry:counts.entrySet()){
            nodes.add(new Node(entry.getKey(), entry.getValue()));
        }
        return nodes;
    }

    private static Node createHuffmanTree(List<Node> nodes){
        while(nodes.size()>1){
            Collections.sort(nodes);
            Node leftNode = nodes.remove(0);
            Node rightNode = nodes.remove(0);
            Node parent = new Node(null,leftNode.weight+rightNode.weight);
            parent.left = leftNode;
            parent.right = rightNode;
            nodes.add(parent);
        }
        return nodes.get(0);
    }

    private static void unZip(String zipFile,String dstFile){
        InputStream is = null;
        ObjectInputStream ois = null;
        OutputStream os = null;
        try{
            is = new FileInputStream(zipFile);
            ois = new ObjectInputStream(is);
            byte[] huffmanBytes = (byte[]) ois.readObject();
            Map<Byte,String> huffmanCodes = (Map<Byte,String>) ois.readObject();
            byte[] bytes = decode(huffmanCodes,huffmanBytes);
            os = new FileOutputStream(dstFile);
            os.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                os.close();
                ois.close();
                is.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private static byte[] decode(Map<Byte, String> huffmanCodes, byte[] huffmanBytes) {
        return new byte[0];
    }

    public static void zipFile(String srcFile,String dstFile){
        OutputStream os = null;
        ObjectOutputStream oos = null;
        FileInputStream fis = null;
        try{
            fis = new FileInputStream(srcFile);
            byte[] b = new byte[fis.available()];
            fis.read(b);
            byte[] huffmanBytes = huffmanZip(b);
            os = new FileOutputStream(dstFile);
            oos = new ObjectOutputStream(os);
            oos.writeObject(huffmanBytes);
            oos.writeObject(huffmanCodes);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static String byteToBitSString(boolean flag, byte b){
        int temp = b;
        if (flag){
            temp |= 256;
        }
        String str = Integer.toBinaryString(temp);
        if (flag){
            return str.substring(str.length()-8);
        }else {
            return str;
        }
    }
}


