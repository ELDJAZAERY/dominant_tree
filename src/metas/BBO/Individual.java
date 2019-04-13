package metas.BBO;

import data.representations.Solutions.Solution;
import java.util.List;


public class Individual implements Comparable<Individual> {

    // Static #Nb Total of solutions
	private static int totalEvals = 0 ;

	/** Solution **/
	public  Solution sol;
	float fitness;


	// Time & iteration & nbSolution
    private int   eval;
	private float sec = 0;
    private int   nbIteration = 0;


	public Individual(List<Integer> CurrentSol){
        Solution sol = new Solution(CurrentSol);

        this.sol = sol;
        this.fitness = sol.fitness;

        this.eval = ++totalEvals;
    }


    public Individual(){
        Solution newSol = new Solution();
        sol = newSol;
        fitness = sol.fitness;
        eval = ++totalEvals;
    }


    public void display() {

	    if(sec == 0 ){
            long endTime_best = System.currentTimeMillis();
            sec =  (endTime_best - BBO.startTime);
            sec = (float) (sec/1000.1);
            nbIteration = BBO.nbIteration;
        }

	    System.out.println(this.toString());
    }


    @Override
    public String toString() {
        String out = "";
        out += "Best {";
        out += "\n\t Fitness    : " + sol.fitness;
        out += "\n\t Vertices   : " + sol.verticesDT.size();
        out += "\n\t Iteration  : " + nbIteration;
        out += "\n\t nb Sols    : " + eval;
        out += "\n\t Secs       : " + sec;
        out += "\n}";
        return out;
    }

    @Override
    public int compareTo(Individual other) {
        return (int)sol.fitness - (int)other.sol.fitness;
    }

}
