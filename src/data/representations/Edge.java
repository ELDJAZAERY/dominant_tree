package data.representations;

import java.util.ArrayList;
import java.util.List;

public class Edge implements Comparable<Edge>  {

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
        if (!(other instanceof Edge)) {
            return false;
        }
        Edge e = (Edge) other;
        return e.one.equals(this.one) && e.two.equals(this.two);
    }

	@Override
    public int compareTo(Edge other) {
        float diff = this.weight - other.weight;
        return diff < 0 ? -1 : diff > 0 ? 1 : 0;
    }

    @Override
    public int hashCode() {
        return (one.getLabel() + "," + two.getLabel()).hashCode();
    }


}
