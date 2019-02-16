package methas.VNS;

public class VNS_Algo {


    public static void Exec(int Kmax , int MaxIter){
        int iter = 0 , k = 2;

        VNSolution  currentSolution ,BestSolution , tempSolution  ;

        BestSolution = new VNSolution();


        while (iter < MaxIter){

            // Solution initial
            currentSolution = new VNSolution();

            k = 2;
            while(k <= Kmax){
                tempSolution = currentSolution.shaking(k);
                tempSolution = tempSolution.LocalSearch(k);
                if(BestSolution.compareTo(tempSolution)>0){
                    System.out.println("lastBest :"+BestSolution.fitness()+" newBest :"+tempSolution.fitness());
                    System.out.println(BestSolution.compareTo(tempSolution)>0);
                    BestSolution = (VNSolution) tempSolution.clone();
                    currentSolution = (VNSolution) tempSolution.clone();
                    k = 2;
                    //System.out.println("Best Sol ---> "+BestSolution.fitness());
                }

                k++;
            }
        }
    }

}
