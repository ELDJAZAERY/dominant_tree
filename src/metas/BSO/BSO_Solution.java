package metas.BSO;


import data.reader.Instances;
import data.representations.Solutions.Binary_Solution;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ThreadLocalRandom;


public class BSO_Solution extends Binary_Solution implements Comparable , Cloneable {

    public static int flip = 5;

    public static int nbLocalSearch = 10;

    public BSO_Solution(){
        super();

        int i = 1;
        do{
            int flipper = ( (flip * i )% Instances.NbVertices-1) ;
            binary[flipper] = (byte) ( (binary[flipper] + 1) % 2) ;
            i++;
        }while (flip * i  < Instances.NbVertices );

        Binary_TO_Set();
    }

    public BSO_Solution(BSO_Solution sol , int numBee){
        super();

        BSO_Solution newSol = (BSO_Solution) sol.clone();
        Byte[] binary = newSol.binary;

        int i = 1;
        do{
            int fliper = ( (flip * i + numBee)% Instances.NbVertices - 1 ) ;
            binary[fliper] = (byte) ( (binary[fliper] + 1) % 2) ;
            i++;
        }while (flip * i + numBee < Instances.NbVertices );


        this.binary = binary;
        Binary_TO_Set();
    }

    public BSO_Solution(BSO_Solution clone){
        super(clone);
    }

    public int distance(BSO_Solution sol){
        int dist = 0;
        for(int i = 0 ; i < binary.length ; i++ ) {
            if(binary[i] != sol.binary[i])
                dist++;
        }
        return dist;
    }

    public int distance(){
        int dist = 0;
        for(int i = 0 ; i < binary.length ; i++ ) {
            if(binary[i].equals(BSO_Algo.CurrentSol.binary[i]))
                dist++;
        }
        return dist;
    }


    public BSO_Solution diversity(){

        Collections.sort(Bee.Dances,
                Comparator.comparingInt(BSO_Solution::distance));

        if(!Bee.Dances.isEmpty())
            return Bee.Dances.get(Bee.Dances.size()-1);

        return BSO_Algo.CurrentSol;
    }


//    public BSO_Solution diversity(){
//        BSO_Solution tempSol ;
//
//        Collections.sort(BSO_Algo.tabu,
//                Comparator.comparingInt(BSO_Solution::distance));
//
//        if(!BSO_Algo.tabu.isEmpty()){
//            tempSol = BSO_Algo.tabu.get(BSO_Algo.tabu.size()-1);
//            BSO_Algo.tabu.remove(tempSol);
//            return tempSol;
//        }
//
//        return BSO_Algo.CurrentSol;
//    }


    public void LocalSearch(){
        BSO_Solution sol ;

        for(int i = 1 ; i <= nbLocalSearch ; i++){
            sol = (BSO_Solution) this.clone();

            int randIndex = ThreadLocalRandom.current()
                    .nextInt(0,sol.binary.length);

            sol.binary[randIndex] = (sol.binary[randIndex] == 0)? (byte)1 : (byte)0;
            sol.Binary_TO_Set();

            if(compareTo(sol)>0){
                binary = sol.binary;
                Binary_TO_Set();
            }
        }
    }

    public void printPerformance(){
        System.out.println(fitness);
    }

    @Override
    public int compareTo(Object other) {
        return ((int)fitness - (int)((BSO_Solution) other).fitness);
    }

    @Override
    public BSO_Solution clone() {
        return new BSO_Solution(this);
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
