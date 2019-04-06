package metas.GA;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;


public class GA {

    private int populationSize , nbIteration;

    public GA(int populationSize , int nbIteration){
        this.populationSize = populationSize;
        this.nbIteration = nbIteration;
    }


    public void Exec(){

        ArrayList<Individual> population = new ArrayList<>();
        Individual n , n1 , nstar , BestSol = new Individual();
        int nbIter = nbIteration;

        // init population initial
        int init = populationSize + 1;
        while(--init != 0) {
            population.add(new Individual());
        }


        ArrayList<Individual> newPopulation ;


        while(--nbIter != 0){

            Collections.sort(population);

            newPopulation = new ArrayList<>();


            for(int i = 0 ; i < populationSize-3 ; i++){

                // get Have Max fitness
                n = population.get(i);

                // i % K
                n1 = population.get(i+1);


                /** #CrossOver **/
                nstar = Individual.crossOver(n,n1);


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

        System.out.println(this.nbIteration);
    }



}
