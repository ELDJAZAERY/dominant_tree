package metas.defaultMetha;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Heuristic {

    int Kmax , MaxIter;


    public Heuristic(int Kmax , int MaxIter ){
        this.Kmax = Kmax;
        this.MaxIter = MaxIter;
    }


    public void Exec(){
        int iter = 0;

        Individual currentSolution ,BestSolution ;

        BestSolution = currentSolution = new Individual();

        ArrayList<Individual> LastBest = new ArrayList<>();

        LastBest.add(BestSolution);


        int init = 1000;
        while(--init != 0) {
            currentSolution = new Individual();
            if(BestSolution.compareTo(currentSolution)>0){
                BestSolution = currentSolution;
                BestSolution.printPerformance();
                //System.out.println(" --- INITIAL SOLUTIONS ----");
            }
        }


        BestSolution.printPerformance();
        int JUMPING = 600;
        while (++iter <= MaxIter) {
            if(--JUMPING == 0 && !LastBest.isEmpty()) {
                //currentSolution = new Individual();
                int randIndex = ThreadLocalRandom.current().nextInt(0,LastBest.size());
                currentSolution = LastBest.get(randIndex);
                LastBest.remove(randIndex);
                JUMPING = 600;
                System.out.println(" --- JUMPING ---");
            }

            for(int k =1; k<Kmax ; k++){

                // @Shaking
                currentSolution = currentSolution.shaking(k);

                if(BestSolution.compareTo(currentSolution)>0){
                    BestSolution = currentSolution;
                    LastBest.add(BestSolution);
                    BestSolution.printPerformance();
                    k=1;
                }

                // @ LocalSearch
                currentSolution = currentSolution.LocalSearch(k);

                if(BestSolution.compareTo(currentSolution)>0){
                    BestSolution = currentSolution;
                    LastBest.add(BestSolution);
                    BestSolution.printPerformance();
                    k=1;
                }

            }

        }

        System.out.println(" ----- defaultMetha FIN -----");
    }



}
