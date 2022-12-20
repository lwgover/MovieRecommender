package graph;

import java.util.Arrays;

public class GraphTesting {
    public static void main(String[] args){
        Graph<Integer> testing = new Graph<>();
        testing.addVertex(0);
        testing.addVertex(1);
        testing.addVertex(2);
        testing.addVertex(3);
        testing.addEdge(0, 1);
        testing.addEdge(1, 2);
        testing.addEdge(2, 3);

        System.out.println(Arrays.toString(GraphAlgorithms.dijkstrasAlgorithm(testing, 0)));
    }
}
