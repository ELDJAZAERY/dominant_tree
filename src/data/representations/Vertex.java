package data.representations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class Vertex implements Cloneable {

    public  int index ;
    private String label;
    private HashSet<Vertex> neighbors;
	private ArrayList<Edge> neighborsEdges;


	public Vertex(String label) {
		this.label = label;
		index = Integer.valueOf(label);
		neighborsEdges = new ArrayList<>();
		neighbors = new HashSet<>();
	}

	private Vertex(Vertex clone){
	    label = clone.label;
	    neighbors = new HashSet<>();
	    neighborsEdges = new ArrayList<>();
    }

    public ArrayList<Edge> getNeighborsEdges() {
        return neighborsEdges;
    }

    public void addNeighbor(Edge edge){
		if(!neighborsEdges.contains(edge))
		    neighborsEdges.add(edge);
	}

    public void addNeighbor(Vertex neighbor) {
        neighbors.add(neighbor);
    }

    /** Getters **/

	public Edge getNeighbor(int index) { return neighborsEdges.get(index); }

	public int getNeighborCount() { return neighborsEdges.size(); }

	public String getLabel() {
		return label;
	}

    public HashSet<Vertex> getNeighbors() {
        return neighbors;
    }


    /** Helpers **/

    public HashSet<Vertex> getDominNeighbors(List<Vertex> dominVertices) {
        HashSet<Vertex> dominNeighbors = new HashSet<>();
        for(Vertex v : neighbors)
            if(dominVertices.contains(v))
                dominNeighbors.add(v);

        return dominNeighbors;
    }

    public HashSet<Edge> getDominNeighborsEdges(List<Vertex> dominVertices) {
        HashSet<Edge> dominNeighbors = new HashSet<>();
        for(Edge edge: neighborsEdges){
            if(edge.isDomin(dominVertices))
                dominNeighbors.add(edge);
        }
        return dominNeighbors;
    }

    private boolean isNeighbor(Vertex other){
	    return neighbors.contains(other);
    }

    public boolean isNeighbor(HashSet<Vertex> vertices){
        for(Vertex v:vertices)
            if(isNeighbor(v)) return true;
        return false;
    }

    public boolean isPurninable(HashSet<Vertex> verticesDT){
	    if(nbDominNeighbors(verticesDT)>1) return false;

	    for(Vertex neighbor:neighbors)
            if(!neighbor.haveAnotherDominNeighbor(this,verticesDT))
                return false;
	    return true;
    }

    private int nbDominNeighbors(HashSet<Vertex> verticesDT){
	    int cpt = 0;
	    for(Vertex v:verticesDT){
	        if(neighbors.contains(v))
	            cpt++;
        }
        return cpt;
    }

    private boolean haveAnotherDominNeighbor(Vertex vertex , HashSet<Vertex> verticesDT){
        for(Vertex neighbor : neighbors){
            if(verticesDT.contains(neighbor) &&
                    !neighbor.equals(vertex))
                return true;
        }
        return false;
    }


    @Override
    public String toString() {
        return " " + label;
    }

    @Override
    public int hashCode() {
        return label.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Vertex)) {
            return false;
        }
        Vertex v = (Vertex) other;
        return this.label.equals(v.label);
    }

    @Override
    protected Object clone() {
        return new Vertex(this);
    }

}
