package graph;

import data.Movie;

import javax.xml.crypto.dom.DOMCryptoContext;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

public class GraphAlgorithms {
    /**
     * Finds the node with the maximum degree within a graph and return's it's degree
     *
     * @param g The graph to find the max degree in
     * @return Maximum Degree
     */
    public static int maxDegree(GraphIfc<Integer> g){
        int maxDegree = 0;
        for(Integer n: g.getVertices()){
            int numNeighbors = g.getNeighbors(n).size();
            if (numNeighbors > maxDegree){
                maxDegree = numNeighbors;
            }
        }
        return maxDegree;
    }
    /**
     * Finds the 2 nodes in which the shortest path between them is the greatest out of all the combinations of 2 nodes
     *
     * @param g The graph to find the longestShortestPath of
     * @return returns the length of the longest shortest path in [0], the start node in [1] and the end node in [2]
     */
    public static int[] longestShortestPath(GraphIfc<Integer> g){
        int[][] shortestPaths = floydWarshall(g);
        int longestShortestPath = 0;
        int start = -1;
        int end = -1;
        for(int i = 0; i < shortestPaths.length; i++){
            for(int j = 0; j < shortestPaths[i].length; j++){
                if(shortestPaths[i][j] > longestShortestPath && shortestPaths[i][j] != Integer.MAX_VALUE){
                    longestShortestPath = shortestPaths[i][j];
                    start = i;
                    end = j;
                }
            }
        }
        int[] temp = new int[3];
        temp[0] = longestShortestPath;
        temp[1] = start;
        temp[2] = end;
        return temp;
    }
    /**
     * Finds the average path length of every possible path on a graph
     *
     * @param g the graph to check
     * @return The average path length on the graph
     */
    public static double averagePathLength(GraphIfc<Integer> g){
        int[][] shortestPaths = floydWarshall(g);
        double totalPathLength = 0;
        double totalPaths = 0;
        for(int i = 0; i < shortestPaths.length; i++){
            for(int j = 0; j < shortestPaths[i].length; j++){
                if(shortestPaths[i][j] != Integer.MAX_VALUE){
                    totalPathLength += shortestPaths[i][j];
                    totalPaths += 1;
                }
            }
        }
        return totalPathLength / totalPaths;
    }
    /**
     * all nodes shortest path from source node
     *
     * @param graph on which to find shortest path
     * @param source source node
     * @return int array in which arr[i] returns the previous node on the shortest path to source from i
     */
    public static int[] dijkstrasAlgorithm(GraphIfc<Integer> graph, int source) {
        PriorityQueue Q = new PriorityQueue();
        int[] dist = new int[graph.numVertices()];
        int[] prev = new int[graph.numVertices()];

        dist[source] = 0;

        for (int vertex : graph.getVertices()) {
            if (vertex != source) {
                dist[vertex] = Integer.MAX_VALUE;
            }

            Q.push(dist[vertex], vertex);
        }

        while (!Q.isEmpty()) {
            int u = Q.pop();

            for (int v : graph.getNeighbors(u)) {
                int alt = Math.max(dist[u], dist[u] + 1);
                if (alt < dist[v]) {
                    dist[v] = alt;
                    prev[v] = u;
                    Q.changePriority(alt, v);
                }
            }
        }

        return prev;
    }
    /**
     * All node shortest path algorithm
     *
     * @param graph graph in which to find the shortest path between nodes
     * @return int[][] in which arr[i][j] returns the shortest distance between nodes i and j
     */
    public static int[][] floydWarshall(GraphIfc<Integer> graph){
        int[][] D = new int[graph.numVertices()][];
        for(int i = 0; i < graph.numVertices(); i++){
            D[i] = new int[graph.numVertices()];
            Collection<Integer> neighbors = graph.getNeighbors(i);
            for(int j = 0; j < graph.numVertices();j++){
                if(i == j){ // getting from 1 to 1 requires 0 cost
                    D[i][j] = 0;
                }else if(neighbors.contains(j)){
                    D[i][j] = (int)graph.getEdgeWeight(i,j); //Weight of Line
                }else{
                    D[i][j] = Integer.MAX_VALUE;
                }
            }
        }
        int[][] prevs;
        for(int k = 1; k < graph.numVertices(); k++){
            prevs = D;
            D = new int[graph.numVertices()][graph.numVertices()];
            for(int i = 0; i < graph.numVertices(); i++){
                for(int j = 0; j < graph.numVertices(); j++){
                    if(prevs[i][k] != Integer.MAX_VALUE && prevs[k][j] != Integer.MAX_VALUE){
                        D[i][j] = Math.min(prevs[i][k] + prevs[k][j], prevs[i][j]);
                    }else{
                        D[i][j] = prevs[i][j];
                    }
                }
            }
        }
        return D;
    }
}
