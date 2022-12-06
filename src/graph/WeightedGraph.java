package graph;

import java.util.*;

public class WeightedGraph<V> implements GraphIfc<V>{

    private Map<V,Map<V,Double>> graph = new HashMap<V,Map<V,Double>>();
    private int numEdges = 0;
    /**
     * Returns the number of vertices in the graph
     * @return The number of vertices in the graph
     */
    public int numVertices() {
        return graph.size();
    }
    /**
     * Returns the number of edges in the graph
     * @return The number of edges in the graph
     */
    public int numEdges() {
        return numEdges;
    }
    /**
     * Removes all vertices from the graph
     */
    public void clear() {
        graph = new HashMap<V,Map<V,Double>>();
        numEdges = 0;
    }
    /**
     * Adds a vertex to the graph. This method has no effect if the vertex already exists in the graph.
     * @param v The vertex to be added
     */
    public void addVertex(V v) {
        graph.put(v,new HashMap<V,Double>());
    }

    @Override
    public void addEdge(V u, V v) {
        if( !containsVertex(u)  || !containsVertex(v)){ throw new IllegalArgumentException();}
        numEdges += 1;
        graph.get(u).put(v,(double)1);
    }

    /**
     * Adds an edge between vertices u and v in the graph.
     *
     * @param u A vertex in the graph
     * @param v A vertex in the graph
     * @throws IllegalArgumentException if either vertex does not occur in the graph.
     */
    public void addEdge(V u, V v, double weight) {
        if( !containsVertex(u)  || !containsVertex(v)){ throw new IllegalArgumentException();}
        numEdges += 1;
        graph.get(u).put(v,weight);
    }
    /**
     * Returns the set of all vertices in the graph.
     * @return A set containing all vertices in the graph
     */
    public Set<V> getVertices() {
        return graph.keySet();
    }
    /**
     * Returns the neighbors of v in the graph. A neighbor is a vertex that is connected to
     * v by an edge. If the graph is directed, this returns the vertices u for which an
     * edge (v, u) exists.
     *
     * @param v An existing node in the graph
     * @return All neighbors of v in the graph.
     * @throws IllegalArgumentException if the vertex does not occur in the graph
     */
    public Collection<V> getNeighbors(V v) {
        if( !containsVertex(v)){ throw new IllegalArgumentException();}
        return graph.get(v).keySet();
    }
    /**
     * Determines whether the given vertex is already contained in the graph. The comparison
     * is based on the <code>equals()</code> method in the class V.
     *
     * @param v The vertex to be tested.
     * @return True if v exists in the graph, false otherwise.
     */
    public boolean containsVertex(V v) {
        return graph.containsKey(v);
    }
    /**
     * Determines whether an edge exists between two vertices. In a directed graph,
     * this returns true only if the edge starts at v and ends at u.
     * @param v A node in the graph
     * @param u A node in the graph
     * @return True if an edge exists between the two vertices
     * @throws IllegalArgumentException if either vertex does not occur in the graph
     */
    public boolean edgeExists(V v, V u) {
        if( !containsVertex(u)  || !containsVertex(v)){ throw new IllegalArgumentException();}
        return graph.get(v).containsKey(u);
    }

    /**
     * Returns the weight of the Edge between v and u
     * @param v Starting node of the edge
     * @param u Ending node of the edge
     * @return weight W of the edge between v and u
     * @throws IllegalArgumentException if either vertex does not occur in the graph
     */
    public double getEdgeWeight(V v, V u){
        assert edgeExists(v,u);
        return graph.get(v).get(u);
    }

    /**
     * Returns the degree of the vertex. In a directed graph, this returns the outdegree of the
     * vertex.
     * @param v A vertex in the graph
     * @return The degree of the vertex
     * @throws IllegalArgumentException if the vertex does not occur in the graph
     */
    public int degree(V v) {
        if(!containsVertex(v)){ throw new IllegalArgumentException();}
        return graph.get(v).size();
    }

    /**
     * Returns a string representation of the graph. The string representation shows all
     * vertices and edges in the graph.
     * @return A string representation of the graph
     */
    public String toString(){
        StringBuilder s = new StringBuilder();
        for (V v : graph.keySet()) {
            s.append(v.toString());
            s.append(" ==> [");
            for(V x : graph.get(v).keySet()){
                s.append(x.toString());
                s.append(", ");
            }
            s.delete(s.length() - 2, s.length());
            s.append("]");
            s.append("\n");
        }
        return s.toString();
    }
}