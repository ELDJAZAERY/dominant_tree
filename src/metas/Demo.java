 package metas;

import data.reader.Instances;
import data.representations.Solutions.Solution;
import metas.ACO.ACO;
import metas.BBO.BBO;
import metas.BSO.BSO;
import metas.GA.GA;


 public class Demo {

	// TODO connect and isConnected Function optimisation

	public static void main(String[] args) {

        String path = "bench_marks\\100\\50_1.txt";
        new Instances(path);


        //MetasDemo(MetasEnum.BBO.name());
        MetasDemo(MetasEnum.ACO.name());
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

	private static void BBODemo(){
        BBO bbo;
        for(int i=0;i<20;i++) {
            BBO.BBO_Exec(40,10,0.05);
            Solution.NbEvalsTotal = 0;
        }
    }

    private static void BBOTime(){
	    BBO.BBOTime();
    }


}
