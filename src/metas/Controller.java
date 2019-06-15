package metas;

import data.representations.Solutions.Solution;
import javafx.concurrent.Task;
import metas.ACO.ACO;
import metas.BBO.BBO;
import metas.BSO.BSO;
import metas.GA.GA;

import java.util.HashMap;

public class Controller {

    public static float CurrentFitness = 0;
    public static int CurrentTimeEnSecs = 0;

    private static Task task;

    private static int nbIterationTotal = 0;
    private static HashMap<MetasEnum,Integer> fonctionPerCen;

    public static void init(){
        fonctionPerCen = new HashMap<>();
        CurrentFitness = 0;
        CurrentTimeEnSecs = 0;
    }

    public static void lance(MetasEnum meta , Task newTask){
        Controller.init();
        task = newTask;
        switch (meta.name()){
            case "ACO" :
                /** ACO TEST **/
                ACO.ACO_Exec();
                break;
            case "BBO" :
                /** BBO TEST **/
                BBO.BBO_Exec();
                break;
            case "GA"  :
                /** Genetic TEST  **/
                GA.Exec();
                break;
            case "Cooperation" :
                /**  Cooperation TEST **/
                Coopertaion.Cooperate();
                break;
        }
    }

    public static boolean isStoped(){
        boolean stopped = false;
        stopped = (task != null && task.isCancelled());
        return stopped;
    }

    public static void majFitness(Solution sol){
        if(sol.fitness < CurrentFitness || CurrentFitness == 0){
            sol.display();
            CurrentFitness = sol.fitness;
        }
    }

    public static int getCurrentTimeEnSecs(){
        return CurrentTimeEnSecs ;
    }

    public static float getCurrentFitness(){
        return CurrentFitness;
    }

}
