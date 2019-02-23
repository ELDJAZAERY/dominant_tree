package methas.defaultMetha;

public class dAlgo {


    public static void Exec(int Kmax , int MaxIter){
        int iter = 0;

        dSolution currentSolution ,BestSolution ;

        BestSolution = currentSolution = new dSolution();

        int init = 1000;
        while(--init != 0) {
            currentSolution = new dSolution();
            if(BestSolution.compareTo(currentSolution)>0){
                BestSolution = currentSolution;
                BestSolution.printPerformance();
            }
        }


        int JUMPING = 50;
        while (++iter <= MaxIter) {
            if(--JUMPING == 0) {
                currentSolution = new dSolution();
                JUMPING = 50;
                System.out.println(" -- JUMPING --");
            }

            for(int k =1; k<Kmax ; k++){

                // @Shaking
                currentSolution = currentSolution.shaking(k);

                if(BestSolution.compareTo(currentSolution)>0){
                    BestSolution = currentSolution;
                    BestSolution.printPerformance();
                    k=1;
                }

                // @ LocalSearch
                currentSolution = currentSolution.LocalSearch(k);

                if(BestSolution.compareTo(currentSolution)>0){
                    BestSolution = currentSolution;
                    BestSolution.printPerformance();
                    k=1;
                }

                currentSolution = BestSolution;
            }

        }

        System.out.println(" ----- defaultMetha FIN -----");
    }

}
