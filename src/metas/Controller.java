package metas;

import metas.ACO.ACO;
import metas.BBO.BBO;
import metas.BSO.BSO;
import metas.GA.GA;

public class Controller {

    public static float CurrentFitness = 0;
    public static int CurrentTimeEnSecs = 0;

    public static void lance(MetasEnum meta){
        switch (meta.name()){
             case "ACO" :
                /** ACO TEST **/
                ACO.ACO_Exec();
                break;
            case "BBO" :
                /** BBO TEST **/
                BBO.BBO_Exec();
                break;
            case "BSO" :
                /**  BSO TEST **/
                BSO.Exec();
                break;
            case "GA"  :
                /** Genetic TEST  **/
                GA.Exec();
                break;
        }
    }

    public static void init(){
        CurrentFitness = 0;
        CurrentTimeEnSecs = 0;
    }

    public static void majFitness(float fitness){
        if(fitness < CurrentFitness || CurrentFitness == 0) CurrentFitness = fitness;
    }

    public static int getCurrentTimeEnSecs(){

        return CurrentTimeEnSecs ;
    }

    public static float getCurrentFitness(){
        return CurrentFitness;
    }

}
