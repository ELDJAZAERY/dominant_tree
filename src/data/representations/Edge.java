package data.representations;

import java.util.LinkedList;
import java.util.List;

public class Edge implements Comparable<Edge> {

	private Vertex one, two;
	private float weight;


	public Edge(Vertex one, Vertex two, float weight) {
		this.one = (one.getLabel().compareTo(two.getLabel()) <= 0) ? one : two;
		this.two = (this.one.equals(one)) ? two : one;
		this.weight = weight;
	}


	/** Getters **/

    public Vertex getOne() {
        return this.one;
    }

    public Vertex getTwo() {
        return this.two;
    }

    public float getWeight() {
        return this.weight;
    }



    /** Helpers **/

    public Vertex getTheOtherOne(Vertex current) {
		if(!(current.equals(one) || current.equals(two)))
			return null;
		return (current.equals(one)) ? two : one;
	}

	public boolean isDomin(List<Vertex> DominVertices){
        return (DominVertices.contains(one) && DominVertices.contains(two));
    }

	public boolean contains_vertex(List<Vertex> verticesDT){
		if(verticesDT.contains(one) && verticesDT.contains(two))
			return true;
		return false;
	}


    @Override
    public String toString() {
        return "({" + one + ", " + two + "}, " + weight + ")";
    }

    @Override
    public boolean equals(Object other) {
        return hashCode() == other.hashCode();
    }

	@Override
    public int compareTo(Edge other) {
        return ((int)this.weight - (int)other.weight);
    }

    @Override
    public int hashCode() {
        return (one+ "," + two).hashCode();
    }

}
