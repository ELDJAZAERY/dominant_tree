package metas.GA;

import data.reader.Instances;
import data.representations.Solutions.Solution;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Individual implements Comparable {


    protected Solution solLocal;


    public Individual(){
        Solution sol = new Solution();
        this.solLocal = sol;
    }

    public Individual(Solution sol) {
        solLocal = (Solution) sol.clone();
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

        this.solLocal = new Solution(new ArrayList<>(crossed));
    }

    public static Individual crossOver(Individual one,Individual otherOne){
        return new Individual(one.solLocal,otherOne.solLocal);
    }

    public void mutation(){
        int randIndex = ThreadLocalRandom.current()
                .nextInt(0, solLocal.permutation.size());

        int mutated = solLocal.permutation.remove(randIndex);
        solLocal.permutation.add(mutated);

        // Correction phase
        solLocal.Correction();
    }


    public void LocalSearch(){
        int temp;
        Solution current ;
        Solution LocalBest = solLocal;
        ArrayList<Integer> permutationTemp;

        for (int d = 0; d < solLocal.verticesDT.size(); d++) {
            for (int v = solLocal.verticesDT.size(); v < Instances.NbVertices ; v++) {
                permutationTemp = new ArrayList<>(solLocal.permutation);
                temp = permutationTemp.get(d);
                permutationTemp.set(d, permutationTemp.get(v));
                permutationTemp.set(v, temp);

                current = new Solution(permutationTemp);

                if (current.fitness < solLocal.fitness) {
                    LocalBest = current;
                }
            }
            solLocal = LocalBest;
        }
    }

    public void display(){
        solLocal.display();
    }

    @Override
    public int compareTo(Object other) {
        return ((int) solLocal.fitness - (int)((Individual) other).solLocal.fitness);
    }

}
