package methas.VNS;

public class VNS_Algo {


    public static void Exec(int Kmax , int MaxIter){
        int iter = 0 , k = 2;

        VNSolution currentSolution ,BestSolution , tempSolution  ;

        BestSolution = new VNSolution();


        while (iter < MaxIter){

            //System.out.println("---- New dSolution ---");
            // dSolution initial
             currentSolution = new VNSolution();
            // fussionner current with bestSol

            k = 2;
            while(k <= Kmax){
                tempSolution = currentSolution.shaking(k);
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
