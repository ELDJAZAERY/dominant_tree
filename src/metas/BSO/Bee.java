package metas.BSO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Bee {

    public static ArrayList<BSO_Solution> Dances = new ArrayList<>();


    static int nbSerachIterations = 10;

    BSO_Solution solution;
    private Set<BSO_Solution> tabuList = new HashSet<>();


    public Bee(){
        solution = new BSO_Solution();
    }
    public Bee(BSO_Solution sol){
        solution = sol;
    }
    public Bee(BSO_Solution sol , int numBee){
        solution = new BSO_Solution(sol,numBee);
    }


    public void search(){

        tabuList.add(solution);

        BSO_Solution tempSolution , BestSolution = solution;

        int cpt = nbSerachIterations;
        while (--cpt >= 0){
            tempSolution = (BSO_Solution) solution.clone();

            if(tabuList.contains(tempSolution)) continue;

            tabuList.add(tempSolution);

            tempSolution.LocalSearch();

            if(BestSolution.compareTo(tempSolution)> 0){
                BestSolution = tempSolution;
            }
        }

        if(!Dances.contains(BestSolution))
            Dances.add(BestSolution);

    }

}
