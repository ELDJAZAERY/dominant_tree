package methas.VNS;

import data.representation.Graph;

import java.util.HashSet;

public class VNS_Algo {


    public static void Exec(int Kmax , int MaxIter){
        int iter = 0 , k = 2;

        VNSolution  currentSolution ,BestSolution , tempSolution  ;

        BestSolution = new VNSolution();


        while (iter < MaxIter){

            //System.out.println("---- New Solution ---");
            // Solution initial
             currentSolution = new VNSolution();
            // fussionner current with bestSol

            k = 2;
            while(k <= Kmax){
                tempSolution = currentSolution.shaking(k);
                tempSolution = tempSolution.LocalSearch(k);
                if(!Graph.isDomiTree(tempSolution.getDominoTree()))
                    continue;
                if(BestSolution.compareTo(tempSolution)>0){
                    BestSolution = (VNSolution) tempSolution.clone();
                    currentSolution = (VNSolution) tempSolution.clone();
                    k = 2;
                    System.out.println("Best Sol ---> "+BestSolution.fitness());
                    System.out.println("Best Sol ---> "+ Graph.isDomiTree(BestSolution.getDominoTree()));

                }

                k++;
            }
        }

        iter++;
        System.out.println(" ----- VNS FIN -----");
    }

}
