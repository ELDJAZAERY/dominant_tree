package metas;

import data.Logger;
import data.reader.Instances;
import data.representations.Solutions.Solution;
import metas.ACO.ACO;
import metas.BBO.BBO;
import metas.BSO.BSO;
import metas.GA.GA;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;


public class Demo {

	// TODO connect and isConnected Function optimisation

    private static int Vertices = 100;
    private static int id = 1 ;


	//public static void main(String[] args) { start(); }

    private static void start(){
        String path = "bench_marks\\100\\200_1.txt";
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
        //BBODemo();
        //BBOTime();

        /** Cooperation Demo **/
        //Coopertaion.Cooperate();

        //CooperationDemo("50_1",10);
        //CooperationDemo("50_2",10);
        //CooperationDemo("50_3",10);

        //ACODemo("50_1",10);
        //CooperationDemo();
        //CooperationDemo();

        GADemo();
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

    private static void GADemo(String nameFile , int nbIteration){

        String path = "bench_marks\\100\\"+nameFile+".txt";
        new Instances(path);

        Logger.LoggerFileName = "Logs/GA100/"+nameFile+".txt";

        for(int i=0;i<nbIteration;i++) {
            GA.Exec();
            Solution.NbEvalsTotal = 0;
            Solution.StartTime = System.currentTimeMillis();

            Logger.PersistanceLog("\n\n ");
            Logger.PersistanceLog(" ------- Fin -------- Ieration N " + i + " \n\n\n\n");
            System.out.println(" ------- Fin -------- Ieration N " + i);
        }

    }
    private static void GADemo(){
        lastVertices("Logs/GA100");

        int lastVertices = Demo.Vertices ;
        int lastId = Demo.id;
        if(lastId == 3){
            lastId = 1;
            lastVertices += 100;
        }else{
            lastId += 1;
        }

        for(int vertices =  lastVertices ; vertices <= 500 ; vertices += 100){
            for(int id = lastId ; id <= 3 ; id++){

                //System.out.println("bench_marks\\100\\"+vertices+"_"+id+".txt");
                String path = "bench_marks\\100\\"+vertices+"_"+id+".txt";
                new Instances(path);

                Logger.LoggerFileName = "Logs/GA100/"+vertices+"_"+id+".txt";

                for(int i=0;i<2;i++) {
                    Solution s = GA.Exec();
                    s.display();
                    Solution.NbEvalsTotal = 0;
                    Solution.StartTime = System.currentTimeMillis();
                    Logger.PersistanceLog(" ------- Fin -------- Ieration N \n\n" + i);
                    System.out.println(" ------- Fin -------- Ieration N " + i);
                }
            }
            lastId = 1;
        }

    }

    private static void ACODemo(String nameFile , int nbIteration){

        String path = "bench_marks\\100\\"+nameFile+".txt";
        new Instances(path);

        Logger.LoggerFileName = "Logs/ACO100/"+nameFile+".txt";


        for(int i=0;i<nbIteration;i++) {
            ACO.ACO_Exec();
            Solution.NbEvalsTotal = 0;
            Solution.StartTime = System.currentTimeMillis();

            Logger.PersistanceLog("\n\n ");
            Logger.PersistanceLog(" ------- Fin -------- Ieration N " + i + " \n\n\n\n");
            System.out.println(" ------- Fin -------- Ieration N " + i);
        }

    }
    private static void ACODemo(){
        for(int vertices = 100 ; vertices <= 500 ; vertices += 100){
            for(int id = 1 ; id <= 3 ; id++){
                String path = "bench_marks\\100\\"+vertices+"_"+id+".txt";
                new Instances(path);

                Logger.LoggerFileName = "Logs/ACO100/"+vertices+"_"+id+".txt";

                for(int i=0;i<10;i++) {
                    ACO.ACO_Exec();
                    Solution.NbEvalsTotal = 0;
                    Solution.StartTime = System.currentTimeMillis();
                    Logger.PersistanceLog(" ------- Fin -------- Ieration N \n\n" + i);
                    System.out.println(" ------- Fin -------- Ieration N " + i);
                }
            }
        }

    }


    private static void BBODemo(String nameFile , int nbIteration){

        String path = "bench_marks\\100\\"+nameFile+".txt";
        new Instances(path);

        Logger.LoggerFileName = "Logs/Cooperation100/"+nameFile+".txt";

        for(int i=0;i<nbIteration;i++) {
            Solution best = Coopertaion.Cooperate();
            Solution.NbEvalsTotal = 0;
            Solution.StartTime = System.currentTimeMillis();

            Logger.PersistanceLog("\n\n "+best.toString());
            Logger.PersistanceLog(" ------- Fin -------- Ieration N " + i + " \n\n\n\n");
            System.out.println(" ------- Fin -------- Ieration N " + i);
        }

    }
    private static void BBODemo(){

        for(int vertices = 100 ; vertices <= 500 ; vertices += 100){
            for(int id = 1 ; id <= 3 ; id++){

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


    private static void CooperationDemo(){

        for(int vertices = 100 ; vertices <= 500 ; vertices += 100){
            for(int id = 1 ; id <= 3 ; id++){

                String path = "bench_marks\\100\\"+vertices+"_"+id+".txt";
                new Instances(path);

                Logger.LoggerFileName = "Logs/Cooperation100/"+vertices+"_"+id+".txt";

                for(int i=0;i<1;i++) {
                    Solution best = Coopertaion.Cooperate();
                    Solution.NbEvalsTotal = 0;
                    Solution.StartTime = System.currentTimeMillis();

                    best.display();
                    Logger.PersistanceLog(" ------- Fin -------- Ieration N " + i + " \n\n\n\n");
                    System.out.println(" ------- Fin -------- Ieration N " + i);
                }
            }
        }

    }
    private static void CooperationDemo(String nameFile , int nbIteration){

        String path = "bench_marks\\100\\"+nameFile+".txt";
        new Instances(path);

        Logger.LoggerFileName = "Logs/Cooperation100/"+nameFile+".txt";

        for(int i=0;i<nbIteration;i++) {
            Solution best = Coopertaion.Cooperate();
            best.display();

            Solution.NbEvalsTotal = 0;
            Solution.StartTime = System.currentTimeMillis();

            Logger.PersistanceLog(" ------- Fin -------- Ieration N " + i + " \n\n\n\n");
            System.out.println(" ------- Fin -------- Ieration N " + i);
        }

    }


    private static void lastVertices(String path){
        File repo = new File(path);
        int maxvertices = 100 , maxid = 0 ;
        int vertices , id ;

        try{
            if (repo.isDirectory()) {
                String[] fileList = repo.list();
                for(String s : fileList){
                    vertices = Integer.parseInt(s.split("_")[0].trim());
                    id = Integer.parseInt(s.split("_")[1].replaceAll("[A-Za-z\\.]",""));
                    if(vertices > maxvertices){
                        maxvertices = vertices;
                        maxid = 1;
                    }
                    if(vertices == maxvertices && id > maxid )
                        maxid = id;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            Demo.Vertices = 100;
            Demo.id = 1 ;
        }

        System.out.println( maxvertices + " ----- " + maxid);

        Demo.Vertices = maxvertices;
        Demo.id = maxid ;
    }

}
