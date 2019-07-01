package metas.GA;

import data.representations.Solutions.Solution;
import metas.Controller;
import metas.Coopertaion;
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

    public static Solution Exec(){
        Individual.withLocalSearch = true;
        GA.nbIteration = 1000;
        GA.populationSize = 25;
        ArrayList<Solution> population = new ArrayList<>();
        initialzePopulation(null);
        Exec(population);
        return population.get(populationSize-1);
    }


    public static void Exec(ArrayList<Solution> populationInitial){
        GA.nbIteration = 15;
        GA.populationSize = 15;
        population = new ArrayList<>();
        initialzePopulation(populationInitial);
        Coopertaion.cooper();
    }

    public static Solution Exec(int nbIter , ArrayList<Solution> populationInitial ){
        return new Individual().solLocal;
    }

    private static void initialzePopulation(ArrayList<Solution> populationInitial){
        int init = populationSize ;

        if(populationInitial == null ){
            while(init-- != 0) {
                population.add(new Individual());
            }
        }else{
            for(Solution sol:populationInitial){
                init--;
                population.add(new Individual(sol));
            }
            while(init-- != 0) {
                population.add(new Individual());
            }
        }
    }

    public static ArrayList<Solution> getPopulation(){
        ArrayList<Solution> popSolutions = new ArrayList<>();
        for(Individual ind:population){
            popSolutions.add(ind.solLocal);
        }
        return popSolutions;
    }

    public static  ArrayList<Solution> GA_ForInitializatoinPopulation(int PopulationSize){
        Individual.withLocalSearch = false;
        Exec(3,PopulationSize);
        return getPopulation();
    }


    public static void GA_Exec(){

        Individual n , n1 , nstar , BestSol = Collections.max(population);
        int nbIter = nbIteration;

        ArrayList<Individual> newPopulation ;
        Controller.init();


        while(!Controller.isStopped()){

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

                //nstar.LocalSearch();
                if(BestSol.compareTo(nstar) > 0 ){
                    BestSol = nstar;
                    Controller.majFitness(BestSol.solLocal);
                }

                newPopulation.add(nstar);
            }

            newPopulation.add(population.get(0));
            newPopulation.add(population.get(1));
            newPopulation.add(population.get(2));

            population = newPopulation;
        }

        System.out.println(" ---- GA END ---- "+nbIteration + " Iterations ");
    }


}
