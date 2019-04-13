package metas.ACO;

import data.representations.Solutions.Solution;

import java.util.ArrayList;


public class ACO_Algo {

    protected static float q0 = (float) 0.005;


    protected static Solution BestSol ;
    private static ArrayList<Ant> Ants = new ArrayList<>();
    private static int nbAnts = 100 , nbIter = 500;

    protected static Double raux = 0.05;


    public static void ACO_Exec(){

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
