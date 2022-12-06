package graph;

import java.util.Arrays;

public class GraphTesting {
    public static void main(String[] args){
        Graph<Integer> g = new Graph<Integer>();
        for(int i = 0; i < 10; i++){
            g.addVertex(i);
        }
        for(int i = 1; i < 10; i++){
            g.addEdge(i-1,i);
            g.addEdge(i, i-1);
        }
        int source = 5;
        //int[] prevs = GraphAlgorithms.dijkstrasAlgorithm(g,source);
        int[][] shortestPaths = GraphAlgorithms.floydWarshall(g);
        for(int i = 0; i < shortestPaths.length; i++){
            System.out.println(Arrays.toString(shortestPaths[i]));
        }
    }
}
