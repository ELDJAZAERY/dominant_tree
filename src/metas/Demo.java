 package metas;

import data.Logger;
import data.reader.Instances;
import data.representations.Solutions.Solution;
import metas.ACO.ACO;
import metas.BBO.BBO;
import metas.BSO.BSO;
import metas.GA.GA;

import java.util.Collections;


public class Demo {

	// TODO connect and isConnected Function optimisation

	public static void main(String[] args) {

        String path = "bench_marks\\100\\300_3.txt";
        new Instances(path);

        //cooperation();
        //MetasDemo(MetasEnum.BBO.name());
        //MetasDemo(MetasEnum.ACO.name());
        //MetasDemo(MetasEnum.BSO.name());
        //MetasDemo(MetasEnum.GA.name());


        /** Initialization population by GA and optimization by BBO **/
        /*
        ga.Exec();
        bbo.BBO_Exec(ga.getPopulation());
        */

        /**  TEST FOR LOGS  **/
        BBODemo();
        //BBOTime();



        /** Cooperation Demo **/
        //Coopertaion.Cooperate();

    }


    private static void MetasDemo(String meta){
        switch (meta){
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

    private static void cooperation(){
        BBO.BBO_Exec(10,10,0.05);
        Solution Best = Collections.max(BBO.getPopulation());
        Best =  ACO.ACO_As_LocalSearch(Best);

        System.out.println("--- Fin ---" + Best.toString());
    }

	private static void BBODemo(){

        for(int vertices = 500 ; vertices <= 500 ; vertices += 100){
            for(int id = 2 ; id <= 3 ; id++){

                String path = "bench_marks\\100\\"+vertices+"_"+id+".txt";
                new Instances(path);

                Logger.LoggerFileName = "Logs/ACO100/"+vertices+"_"+id+".txt";

                for(int i=0;i<20;i++) {
                    ACO.ACO_Exec();
                    Solution.NbEvalsTotal = 0;
                    Solution.StartTime = System.currentTimeMillis();
                    Logger.PersistanceLog(" ------- Fin -------- Ieration N \n\n" + i);
                    System.out.println(" ------- Fin -------- Ieration N " + i);
                }
            }
        }

    }

    private static void BBODemo(int nbVertices , int id){

        String path = "bench_marks\\100\\"+nbVertices+"_"+id+".txt";
        new Instances(path);

        Logger.LoggerFileName = "Logs/BBO100/"+nbVertices+"_"+id+".txt";

        for(int i=0;i<20;i++) {
            BBO.BBO_Exec(40,10,0.005);
            Solution.NbEvalsTotal = 0;
            Solution.StartTime = System.currentTimeMillis();
        }

    }


    private static void BBOTime(){
	    BBO.BBOTime();
    }


}
