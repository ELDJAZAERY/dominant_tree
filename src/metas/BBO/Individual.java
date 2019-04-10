package metas.BBO;


import data.representations.Solutions.Solution;

import java.util.LinkedList;
import java.util.List;


public class Individual implements Comparable<Individual> {

	static int nbEval = 0 ;

	public  Solution sol;
	float cost;
	int eval;

	public Individual(List<Integer> CurrentSol){
        Solution sol = new Solution(CurrentSol);

        this.sol = sol;
        this.cost = sol.fitness;

        this.eval = ++nbEval;
    }

    public Individual(){
        Solution sol = new Solution();

        this.sol = sol;
        this.cost = sol.fitness;

        this.eval = ++nbEval;
    }

    @Override
    public int compareTo(Individual other) {
        return (int)sol.fitness - (int)other.sol.fitness;
    }

}
