package metas.ACO;

import data.representations.Solutions.Solution;

import java.util.ArrayList;


public class ACO {


    private static int nbIter = 500 , nbAnts = 100 ;
    protected static float raux = (float) 0.05;
    protected static float q0 = (float) 0.1;


    protected static Solution BestSol ;
    private static ArrayList<Ant> Ants = new ArrayList<>();


    private static void setParams(){
        ACO.nbIter = 500;
        ACO.nbAnts = 100;
        ACO.raux = (float) 0.05;
        ACO.q0 = (float) 0.05;
    }

    private static void setParams( int nbIteration , int nbAnts , double raux , double q0){
        ACO.nbIter = nbIteration;
        ACO.nbAnts = nbAnts;
        ACO.raux = (float) raux;
        ACO.q0 = (float) q0;
    }


    public static Solution getBest(){
        return BestSol;
    }


    public static void ACO_Exec( int nbIteration , int nbAnts , double raux , double q0){
        setParams(nbIteration,nbAnts,raux,q0);
        Exec();
    }

    public static void ACO_Exec(){
        setParams();
        Exec();
    }

    private static void Exec(){

        BestSol = new Solution();

        init_Ants();
        Ant.Initials_Pheromones();


        for(int iteration = 0; iteration < nbIter; iteration++) {
            for (Ant ant : Ants) {
                ant.build_Solution();
                ant.MAJ_OnLine();
            }
            maj_global();
        }

    }


    private static void init_Ants(){
        Ant ant;
        for(int i=0;i<nbAnts;i++){
            ant = new Ant(i);
            Ants.add(ant);
        }
    }

    private static void maj_global() {
        Ant.MAJ_OffLine(BestSol);
    }

}
