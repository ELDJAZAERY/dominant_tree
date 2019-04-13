package metas.GA;

import data.representations.Solutions.Solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;


public class GA {

    private static int populationSize , nbIteration;
    private static ArrayList<Individual> population ;


    public static void Exec(int nbIteration , int populationSize){
        GA.populationSize = populationSize;
        GA.nbIteration = nbIteration;
        population = new ArrayList<>();
        initialzePopulation(null);
        GA_Exec();
    }

    public static void Exec(){
        GA.nbIteration = 500;
        GA.populationSize = 50;
        population = new ArrayList<>();
        initialzePopulation(null);
        GA_Exec();
    }

    public static void Exec(ArrayList<Solution> populationInitial){
        GA.nbIteration = 500;
        GA.populationSize = 50;
        population = new ArrayList<>();
        initialzePopulation(populationInitial);
        GA_Exec();
    }


    private static void initialzePopulation(ArrayList<Solution> populationInitial){
        int init = populationSize ;

        if(populationInitial == null ){
            while(--init != 0) {
                population.add(new Individual());
            }
        }else{
            for(Solution sol:populationInitial){
                init--;
                population.add(new Individual(sol));
            }
            while(--init != 0) {
                population.add(new Individual());
            }
        }
    }

    public ArrayList<Solution> getPopulation(){
        ArrayList<Solution> popSolutions = new ArrayList<>();
        for(Individual ind:this.population){
            popSolutions.add(ind.solLocal);
        }
        return popSolutions;
    }


    public static void GA_Exec(){

        Individual n , n1 , nstar , BestSol = Collections.max(population);
        int nbIter = nbIteration;

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
                    BestSol.display();
                }

                newPopulation.add(nstar);
            }

            newPopulation.add(population.get(0));
            newPopulation.add(population.get(1));
            newPopulation.add(population.get(2));

            population = newPopulation;
        }

        System.out.println("----- GA END ----"+nbIteration);
    }


}
