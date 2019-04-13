package metas.GA;

import data.representations.Solutions.Solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;


public class GA {

    private int populationSize , nbIteration;
    private ArrayList<Individual> population ;

    public GA(int populationSize , int nbIteration){
        this.populationSize = populationSize;
        this.nbIteration = nbIteration;
        population = new ArrayList<>();
    }


    public void Exec(){

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
                if(proba <= 5)
                    nstar.mutation();

                nstar.LocalSearch();
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

        System.out.println("----- GA END ----"+this.nbIteration);
    }

    public ArrayList<Solution> getPopulation(){
        ArrayList<Solution> popSolutions = new ArrayList<>();
        for(Individual ind:this.population){
            popSolutions.add(ind.solLocal);
        }
        return popSolutions;
    }

}
