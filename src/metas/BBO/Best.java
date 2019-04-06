package metas.BBO;

import data.representations.Graph;
import data.representations.Vertex;
import java.util.LinkedList;

public class Best {
	
	float t;
	float cost;
	int NbIt;
	int NbN;
	int div;
	Individual individual;
	LinkedList<Vertex> verticesDT;
	int clones;
	int nbev;
	
	public Best()
	{
		t = 0;
		cost = 0;
		NbIt = 0;
		div = 0;
		NbN = 0;
		clones = 0;
		nbev = 0;		
	}

	public void update(Individual newBest , int i , long startTime ){
		cost = newBest.cost;
		long endTime_best = System.currentTimeMillis();
		t = (endTime_best - startTime) / 1000;
		NbIt = i + 1;
		div = 0;
        individual = newBest;
		NbN = newBest.sol.verticesDT.size();
		verticesDT = new LinkedList<>(newBest.sol.verticesDT);
		nbev = newBest.eval;
	}

    public void display(){
	    System.out.println(this.toString());
    }

	@Override
	public String toString() {
		String out = "";
		out += "Best {";
		out += "\n\t Fitness    : " + cost;
        out += "\n\t Nodes      : " + NbN;
        out += "\n\t Iters      : " + NbIt;
        out += "\n\t nb Sols    : " + nbev;
		out += "\n\t Secs       : " + t;
		out += "\n}";
		return out;
	}

}
