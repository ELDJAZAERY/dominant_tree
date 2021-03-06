package metas.ACO;

import data.representations.Solutions.Solution;
import metas.Controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ACO {


    private static int nbIter = 1000 , nbAnts = 100 ;
    protected static float raux = (float) 0.05;
    protected static float q0 = (float) 0.1;


    protected static Solution BestSol ;
    private static ArrayList<Ant> Ants = new ArrayList<>();


    private static void setParams(){
        ACO.nbIter = 5;
        ACO.nbAnts = 15;
        ACO.raux = (float) 0.05;
        ACO.q0 = (float) 0.05;
    }

    public static void setParams( int nbIteration , int nbAnts , double raux , double q0){
        ACO.nbIter = nbIteration;
        ACO.nbAnts = nbAnts;
        ACO.raux = (float) raux;
        ACO.q0 = (float) q0;
    }

    public static Solution getBest(){
        return BestSol;
    }


    public static Solution ACO_As_LocalSearch(Solution solInitial) {

        BestSol = (Solution) solInitial.clone();
        Controller.majFitness(BestSol);


        setParams(10,10,0.05,0.05);

        init_Ants();
        Ant.Initials_Pheromones();

        for(int iteration = 0; iteration < nbIter; iteration++) {
            MultiThreadAnts();
            maj_global();
        }

        return (Solution) BestSol.clone();
    }

    public static void ACO_Exec(){
        setParams();
        Exec();
    }


    public static Solution CooperationExec(ArrayList<Solution> populations) {

        if( Ant.pheromone_table == null)
            Ant.Initials_Pheromones();

        BestSol = (Solution) Collections.max(populations).clone();
        Controller.majFitness(BestSol);

        for(Solution s:populations){
            Ant.MAJ_OffLine(s);
        }

        setParams(1,1,0.05,0.05);

        init_Ants();

        for(int iteration = 0; iteration < nbIter; iteration++) {
            for (Ant ant : Ants) {
                ant.build_Solution();
                ant.MAJ_OnLine();
            }
            maj_global();
        }

        return (Solution) BestSol.clone();
    }

    public static void Cooperate(){
        setParams();
        Exec();
    }

    public static Solution CooperationFinalExec() {

        for(int iteration = 0; iteration < nbIter; iteration++) {
            for (Ant ant : Ants) {
                ant.build_Solution();
                ant.MAJ_OnLine();
            }
            maj_global();
        }

        return (Solution) BestSol.clone();
    }


    private static void Exec(){

        BestSol = new Solution();
        Controller.majFitness(BestSol);

        init_Ants();
        Ant.Initials_Pheromones();

        while(!Controller.isStopped()) {
            for (Ant ant : Ants) {
                ant.build_Solution();
                ant.MAJ_OnLine();
            }
            maj_global();
        }

    }

    private static void MultiThreadAnts(){
        ArrayList<Callable<Void>> taskList = new ArrayList<>();
        for (int a = 0; a < Ants.size() ; a++) {
            final int ant = a;
            Callable<Void> callable = () -> {
                Ants.get(ant).build_Solution();
                Ants.get(ant).MAJ_OnLine();
                return null;
            };
            taskList.add(callable);
        }

        ExecutorService executor = Executors.newFixedThreadPool(Ants.size());
        try {
            executor.invokeAll(taskList);
        } catch (InterruptedException ie) {

        }
    }


    private static void init_Ants(){
        Ant ant;
        Ants = new ArrayList<>();
        for(int i=0;i<nbAnts;i++){
            ant = new Ant(i);
            Ants.add(ant);
        }
    }

    private static void maj_global() {
        Ant.MAJ_OffLine(BestSol);
    }

}
