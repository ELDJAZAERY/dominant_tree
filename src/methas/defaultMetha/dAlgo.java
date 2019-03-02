package methas.defaultMetha;

import data.representation.Node;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class dAlgo {


    public static void Exec(int Kmax , int MaxIter){
        int iter = 0;

        dSolution currentSolution ,BestSolution ;

        BestSolution = currentSolution = new dSolution();

        ArrayList<dSolution> LastBest = new ArrayList<>();

        LastBest.add(BestSolution);


        int init = 1000;
        while(--init != 0) {
            currentSolution = new dSolution();
            if(BestSolution.compareTo(currentSolution)>0){
                LastBest.add(BestSolution);
                BestSolution = currentSolution;
                BestSolution.printPerformance();
                System.out.println(" --- INITIAL SOLUTIONS ----");
            }
        }


        BestSolution.printPerformance();
        int JUMPING = 200;
        while (++iter <= MaxIter) {
            if(--JUMPING == 0 && !LastBest.isEmpty()) {
                //currentSolution = new dSolution();
                int randIndex = ThreadLocalRandom.current().nextInt(0,LastBest.size());
                currentSolution = LastBest.get(randIndex);
                LastBest.remove(randIndex);
                JUMPING = 200;
                System.out.println(" --- JUMPING ---");
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

            }

        }

        System.out.println(" ----- defaultMetha FIN -----");
    }



}
