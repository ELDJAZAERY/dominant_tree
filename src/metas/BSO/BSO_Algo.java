package metas.BSO;

import java.util.ArrayList;
import java.util.Collections;

public class BSO_Algo {

    public static int maxChances = 30;
    public static int chance = 10;
    public static int nbBees = 5 ;

    public static BSO_Solution CurrentSol = new BSO_Solution()
            ,BestSol = new BSO_Solution();

    static ArrayList<Bee> Bees = new ArrayList<>();

    public static ArrayList<BSO_Solution> tabu = new ArrayList<>();


    public static void Exec(int nbBee , int flip , int nbIterations){

        nbBees = nbBee;

        BSO_Solution.flip = flip;

        initBee(nbBee);

        while (--nbIterations >= 0){
            selectZone();

            lanceBees();
            //System.out.println(" --- Iteration N --- " + nbIterations);
        }

        System.out.print(" --- BSO Terminated --- ");

        BestSol.printPerformance();
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
            BestSol.printPerformance();

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
            Bees.add(bee);
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
