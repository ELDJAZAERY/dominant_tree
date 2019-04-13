 package metas;

import data.reader.Instances;
import metas.ACO.ACO_Algo;
import metas.BBO.BBO;
import metas.BBO.Individual;
import metas.GA.GA;

import java.util.ArrayList;

public class Demo {

	// TODO connect and isCennected Function optimisation

	public static void main(String[] args) {

        String path = "bench_marks\\100\\50_1.txt";

        new Instances(path);

		BBO bbo = new BBO(300,10,(float) 0.005);
        GA ga = new GA(50,500);


        /** ACO TEST **/
        ACO_Algo.ACO_Exec();


        /** BBO TEST **/
        //bbo.BBO_Exec(null);


        /**  BSO TEST **/
        //BSO_Algo.Exec(10,5,1000);


        /** Genetic TEST  **/
        //ga.Exec();


        /** VNS heuristic **/
        //heuristic.Exec();


        /** Initialization population by GA and optimization by BBO **/
        /*
        ga.Exec();
        bbo.BBO_Exec(ga.getPopulation());
        */



        /**  TEST FOR LOGS  **/
        //BBODemo();
        //BBOTime();
        //randomTest();

    }


	private static void BBODemo(){
        BBO bbo;
        for(int i=0;i<20;i++) {
            bbo = new BBO(40, 10, (float) 0.005);
            bbo.BBO_Exec(null);
        }
    }

    private static void BBOTime(){
        long startTime = System.currentTimeMillis();

        Individual best = new Individual(), current;
        for(int i = 0 ; i< 100 ; i++){
            current = new Individual();
            System.out.println(current.sol.fitness);
            current = new Individual(current.sol.permutation);
            System.out.println(current.sol.fitness);
            if(current.compareTo(best)<0){
                best = current;
            }
        }

        System.out.println(best.sol.fitness);
        long endTime_best = System.currentTimeMillis();
        long t = (endTime_best - startTime) / 1000;
        System.out.println(t);
    }

    private static void randomTest(){
        Individual current , best ;
        ArrayList<Integer> perm = new ArrayList<>();

        perm.add(369);
        perm.add(230);
        perm.add(446);
        perm.add(466);
        perm.add(218);
        perm.add(390);
        perm.add(103);
        perm.add(461);
        perm.add(204);
        perm.add(263);
        perm.add(373);
        perm.add(310);
        perm.add(48);
        perm.add(182);
        perm.add(213);
        perm.add(20);
        perm.add(72);
        perm.add(40);
        perm.add(149);

        current = new Individual(perm);
        System.out.println(current.sol.fitness + current.sol.verticesDT.toString());
    }

}
