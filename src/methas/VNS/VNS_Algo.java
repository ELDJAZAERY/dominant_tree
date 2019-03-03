package methas.VNS;

import methas.defaultMetha.dSolution;

public class VNS_Algo {


    public static void Exec(int Kmax , int MaxIter){
        int iter =0 , k ;

        VNSolution currentSolution ,BestSolution , tempSolution  ;

        BestSolution = currentSolution = new VNSolution();

        int init = 500;
        while(--init != 0) {
            currentSolution = new VNSolution();
            if(BestSolution.compareTo(currentSolution)>0){
                BestSolution = currentSolution;
                BestSolution.printPerformance();
            }
        }


        while (iter < MaxIter){
            k = 2;
            while(k <= Kmax){
                tempSolution = currentSolution.shaking(k);
                if(BestSolution.compareTo(tempSolution)>0) {
                    BestSolution    = tempSolution;
                    currentSolution = tempSolution;
                }

                tempSolution = tempSolution.LocalSearch(k);

                if(BestSolution.compareTo(tempSolution)>0) {
                    BestSolution    = tempSolution;
                    currentSolution = tempSolution;
                    k = 2;
                    BestSolution.printPerformance();
                }
                k++;
            }
            iter++;
        }

        System.out.println(" ----- defaultMetha FIN -----");
    }

}
