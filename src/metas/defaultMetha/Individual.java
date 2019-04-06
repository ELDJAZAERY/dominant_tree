package metas.defaultMetha;


import data.representations.Solutions.Solution;
import java.util.concurrent.ThreadLocalRandom;

public class Individual implements Comparable , Cloneable {

    static int nbEval = 0 ;

    private int eval;
    private Solution sol;


    public Individual(){
        Solution sol = new Solution();
        this.sol = sol;
        eval = ++nbEval;
    }

    public Individual(Individual other){
        this.sol = (Solution) other.sol.clone();
    }

    public Individual shaking(int K){
        Individual clone = (Individual) this.clone();

        for(int i=0 ; i <= K ; i++) {
            clone.mutation();
        }

        // Correction of solution after shaking
        clone.sol.Correction();

        return clone;
    }


    public void mutation(){
        int randIndex = ThreadLocalRandom.current()
                .nextInt(0,sol.permutation.size());

        int mutated = sol.permutation.remove(randIndex);
        sol.permutation.add(mutated);

        // Correction phase
        sol.Correction();
    }


    public Individual LocalSearch(int K){

        Individual cloneSolution = (Individual) this.clone();

        for(int i=1 ; i <= K ; i++) {
            cloneSolution.shaking(K);
        }

        cloneSolution.sol.Correction();

        return cloneSolution;
    }


    public void printPerformance(){
        System.out.println(toString());
    }

    @Override
    public String toString() {
        String out = "";
        out += "Best {";
        out += "\n\t Fitness : " + sol.fitness;
/*        out += "\n\t Nodes   : " + NbN;
        out += "\n\t Iters   : " + NbIt;
        out += "\n\t Secs    : " + t;*/
        out += "\n}";
        return out;
    }

    @Override
    public int compareTo(Object other) {
        return ((int)sol.fitness - (int)((Individual) other).sol.fitness);
    }

    @Override
    protected Object clone() {
        return new Individual(this);
    }

}
