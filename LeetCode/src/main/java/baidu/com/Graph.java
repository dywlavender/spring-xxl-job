package baidu.com;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @ClassName Graph
 * @Description TODO
 * @Author dlavender
 * @Date 2022/9/21 22:17
 * @Version 1.0
 **/
public class Graph {
    private int[][] edges;
    private ArrayList<String> verteList;
    private int numOfEdges;
    public static void main(String[] args) {
        int n = 5;
        String vertexs[] = {"A","B","C","D","E"};
        Graph graph = new Graph(n);
        for (String vertex:vertexs){
            graph.insertVertex(vertex);
        }
        graph.insertEdge(0,1,1);
        graph.insertEdge(0,2,1);
        graph.insertEdge(1,2,1);
        graph.insertEdge(1,3,1);
        graph.insertEdge(1,4,1);
        graph.showGraph();

    }

    public Graph(int n){
        edges = new int[n][n];
        verteList = new ArrayList<>(n);
        numOfEdges = 0;
    }

    public void insertVertex(String vertex){
        verteList.add(vertex);
    }
    public void insertEdge(int v1,int v2,int weight){
        edges[v1][v2] = weight;
        edges[v2][v1] = weight;
        numOfEdges++;
    }
    public int getNumOfEdges(){
        return numOfEdges;
    }
    public int getNumOfVertex(){
        return verteList.size();
    }
    public int getWeight(int v1,int v2){
        return edges[v1][v2];
    }

    public void showGraph(){
        for (int[] link:edges){
            System.out.println(Arrays.toString(link));
        }
    }
}
