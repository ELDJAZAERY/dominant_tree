package data.representations;

import java.util.*;


public class Graph {
    
    public HashMap<String, Vertex> vertices;

    public HashMap<Integer, Edge> edges;
    
    
    public Graph() {
        this.vertices = new HashMap<>();
        this.edges = new HashMap<>();
    }


    public boolean addEdge(Vertex one, Vertex two, float weight) {
        if(one.equals(two)) {
            return false;   
        }
        
        Edge e = new Edge(one, two, weight);

        edges.put(e.hashCode(), e);
        one.addNeighbor(e);
        one.addNeighbor(two);

        two.addNeighbor(e);
        two.addNeighbor(one);
        return true;
    }

    public void addVertex(Vertex vertex) {
        vertices.put(vertex.getLabel(), vertex);
    }


}
