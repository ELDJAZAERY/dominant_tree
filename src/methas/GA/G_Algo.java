package methas.GA;


import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

public class G_Algo {


    public static void Exec(int K , int nbIter){

        ArrayList<GA_Solution> population = new ArrayList<>();
        GA_Solution n , n1 , nstar , BestSol = new GA_Solution();


        // init population initial
        int init = K+1;
        while(--init != 0) {
            population.add(new GA_Solution());
        }


        ArrayList<GA_Solution> newPopulation ;

        while(--nbIter != 0){

            Collections.sort(population);

            newPopulation = new ArrayList<>();


            for(int i = 0 ; i < K-3 ; i++){

                // get Have Max fitness
                n = population.get(i);

                // i % K
                n1 = population.get(i+1);


                /** #CrossOver **/
                //nstar = GA_Solution.crossOver(n,n1);
                nstar = new GA_Solution(n,n1);


                // proba of 5% to do Mutation
                int proba = ThreadLocalRandom.current().nextInt(0,100);
                if(proba <= 75)
                    nstar.mutation();

                nstar = nstar.LocalSearch();
                if(BestSol.compareTo(nstar) > 0 ){
                    BestSol = nstar;
                    BestSol.printPerformance();
                }

                newPopulation.add(nstar);
            }

            newPopulation.add(population.get(0));
            newPopulation.add(population.get(1));
            newPopulation.add(population.get(2));

            population = newPopulation;
        }

    }



}
