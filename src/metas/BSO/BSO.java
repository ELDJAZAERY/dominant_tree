package metas.BSO;

import data.representations.Solutions.Solution;
import metas.ACO.ACO;

import java.util.ArrayList;
import java.util.Collections;

public class BSO {

    protected static int nbIteration = 300;
    protected static int nbBees = 5 ;
    protected static int flip = 5;

    /** Diversification params **/
    private static int maxChances = 30;
    private static int chance = 10;


    protected static ArrayList<Bee> Bees = new ArrayList<>();
    protected static ArrayList<BSO_Solution> tabu = new ArrayList<>();

    protected static BSO_Solution CurrentSol = new BSO_Solution()
            ,BestSol = new BSO_Solution();


    private static void setParams(int nbIterations , int nbBee , int flip ){
        BSO.nbBees = nbBee;
        BSO.flip = flip;
    }

    public static Solution getBest(){
        return BestSol;
    }

    public static void Exec(){
        setParams(300, 5, 5);
        ACO.ACO_Exec();
    }

    public static void Exec(Solution solutionInitial){
        CurrentSol = new BSO_Solution(solutionInitial);
        BestSol = CurrentSol;
        setParams(300, 5, 5);
        BSO_Exec();
    }

    public static void Exec(int nbIterations , int nbBee , int flip) {
        setParams(nbIterations, nbBee, flip);
        BSO_Exec();
    }

    public static void Exec(int nbIterations , int nbBee , int flip , Solution solInitial ){
        CurrentSol = new BSO_Solution(solInitial);
        BestSol = CurrentSol;
        setParams(nbIterations,nbBee,flip);
        BSO_Exec();
    }



    public static void BSO_Exec(){

        initBee(nbBees);
        int iterations = 0;

        while (--iterations < nbIteration){
            selectZone();
            lanceBees();
        }

        System.out.print(" --- BSO Terminated --- ");
        BestSol.display();
    }


    /** diversity and intensity
        with max chance to be better **/
    private static void selectZone(){

        BSO_Solution sBest , fBest;

        //System.out.println(Bees.size());
        //System.out.println(Bee.Dances.size());

        Collections.sort(Bee.Dances);
        sBest = Bee.Dances.remove(0);

        do{
            sBest = Bee.Dances.remove(0);
        }while(tabu.contains(sBest) && !Bee.Dances.isEmpty());

        //System.out.println(Bee.Dances.size());

        CurrentSol = sBest;

        if(!Bee.Dances.isEmpty())
        if(BestSol.compareTo(sBest)>0){
            /** Intensity **/
            chance = maxChances;
            BestSol = sBest;
            CurrentSol = BestSol;
            tabu.add(CurrentSol);

            /*** deploy Current Best Sol Performance */
            BestSol.display();

            Bee.Dances.clear();
            return;
        }else chance--;

        if(Bee.Dances.isEmpty()) Bee.Dances.add(new BSO_Solution());

        if(chance <= 0){
            //System.out.println(" --- Diversity ---");
            /** Diversity **/
            //System.out.println(CurrentSol.equals(CurrentSol.diversity()));
            CurrentSol = CurrentSol.diversity();
            //tabu.add(CurrentSol);
            chance = maxChances;

            Bee.Dances.clear();
            return;
        }

        //System.out.println(" Chance --> " + chance);
        //CurrentSol.printPerformance();

    }


    private static void initBee(int nbBee){
        for(int i = 0 ; i< nbBee ; i++){
            Bee bee = new Bee();
            bee.search();
            BSO.Bees.add(bee);
        }
    }


    private static void lanceBees(){

        Bees.clear();
        Bees.add(new Bee(CurrentSol));

        for(int i = 1 ; i < nbBees ;i++){
            Bees.add(new Bee(CurrentSol,i));
        }

        for(Bee bee:Bees){
            bee.search();
        }

    }


}
