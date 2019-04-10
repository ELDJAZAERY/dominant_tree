package metas.GA;

import data.reader.Instances;
import data.representations.Solutions.Solution;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Individual implements Comparable {

    static int nbEval = 0 ;

    private int eval;
    protected Solution sol;


    public Individual(){
        Solution sol = new Solution();
        this.sol = sol;
        eval = ++nbEval;
    }


    private Individual(Solution s1 , Solution s2){
        ArrayList<Integer> crossed = new ArrayList<>();
        int rand;

        for(int i = 0; i < Instances.NbVertices ; i ++){
            rand = ThreadLocalRandom.current().nextInt(0,100);
            if(rand < 50){
                if(!crossed.contains(s1.permutation.get(i)))
                    crossed.add(s1.permutation.get(i));
            }else{
                if(!crossed.contains(s2.permutation.get(i)))
                    crossed.add(s2.permutation.get(i));
            }
        }

        this.sol = new Solution(new ArrayList<>(crossed));
    }

    public static Individual crossOver(Individual one,Individual otherOne){
        return new Individual(one.sol,otherOne.sol);
    }

    public void mutation(){
        int randIndex = ThreadLocalRandom.current()
                .nextInt(0,sol.permutation.size());

        int mutated = sol.permutation.remove(randIndex);
        sol.permutation.add(mutated);

        // Correction phase
        sol.Correction();
    }


    public Individual LocalSearch(){
        return this;
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

}
