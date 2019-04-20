package metas;

import data.representations.Solutions.Solution;
import metas.ACO.ACO;
import metas.BBO.BBO;
import metas.BSO.BSO;
import metas.GA.GA;

import java.util.*;

import static java.util.stream.Collectors.toMap;

public class Coopertaion {

    public static String CurrentMeta = MetasEnum.BBO.name();


    private static HashMap<String , Double> MetaProbInitial;


    private static ArrayList<Solution> PopulationGeneral ;
    private static Solution BestGeneral ;
    private static HashMap<String , Solution> BestByMeta = new HashMap<>();

    private static HashMap<String , Double> MetaProba ;
    static{
        MetaProbInitial = new HashMap<>();
        MetaProbInitial.put(MetasEnum.ACO.name(),0.25);
        MetaProbInitial.put(MetasEnum.BBO.name(),0.25);
        MetaProbInitial.put(MetasEnum.BSO.name(),0.25);
        MetaProbInitial.put(MetasEnum.GA.name() ,0.25);
    }

    /** Cooperation @Params **/
    public static int populationSize = 20;
    public static int nbIterations = 50;


    public static Solution Cooperate(){

        System.out.println(" -- Cooperate ---");
        PopulationGeneral = GA.GA_ForInitializatoinPopulation(populationSize);
        System.out.println(" --- GA FIN ---");

        BestGeneral = new Solution();
        MetaProba = new HashMap<>(MetaProbInitial);


        Solution currentSol ;

        int nbIterations = Coopertaion.nbIterations;
        while(nbIterations-- > 0){
            MetaProba = MetaProba
                    .entrySet()
                    .stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(
                            toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1 - e2,
                                    LinkedHashMap::new));

            for(String methaName:MetaProba.keySet()){
                System.out.println("meta --> "+ methaName);

                if((currentSol = MetasExec(methaName)).fitness < BestGeneral.fitness){
                    BestGeneral = currentSol;
                    BestGeneral.display();
                    MAJProba(methaName);
                }
            }

            MAJProba();
        }

        return BestGeneral;
    }

    private static void MAJProba(){

        if(MetaProba.get(MetasEnum.ACO.name()) <= 0)
            MetaProba.remove(MetasEnum.ACO.name());
        else if(MetaProba.containsKey(MetasEnum.ACO.name()))
            MetaProbInitial.put(MetasEnum.ACO.name(),MetaProba.get(MetasEnum.ACO.name())-0.05);

        if(MetaProba.get(MetasEnum.BBO.name()) <= 0)
            MetaProba.remove(MetasEnum.BBO.name());
        else if(MetaProba.containsKey(MetasEnum.ACO.name()))
            MetaProbInitial.put(MetasEnum.BBO.name(),MetaProba.get(MetasEnum.BBO.name())-0.05);


        if(MetaProba.get(MetasEnum.BSO.name()) <= 0)
            MetaProba.remove(MetasEnum.BSO.name());
        else if(MetaProba.containsKey(MetasEnum.ACO.name()))
            MetaProbInitial.put(MetasEnum.BSO.name(),MetaProba.get(MetasEnum.BSO.name())-0.05);


        if(MetaProba.get(MetasEnum.GA.name()) <= 0)
            MetaProba.remove(MetasEnum.GA.name());
        else if(MetaProba.containsKey(MetasEnum.ACO.name()))
            MetaProbInitial.put(MetasEnum.GA.name(),MetaProba.get(MetasEnum.GA.name())-0.05);

    }

    private static void MAJProba(String meta){
        MetaProba.put(meta,MetaProba.get(meta) + 0.05);
    }

    private static Solution MetasExec(String meta){
        switch (meta){
            case "ACO" :
                /** ACO TEST **/
                return ACO();
            case "BBO" :
                /** BBO TEST **/
                return BBO();
            case "BSO" :
                /**  BSO TEST **/
                return BSO();
            default: return new Solution();
        }
    }

    private static Solution ACO(){
        Solution BestLocal = ACO.CooperationExec(PopulationGeneral);
        return  BestLocal;
    }

    private static Solution BBO(){
        Solution BestLocal = BBO.CooperationExec(PopulationGeneral);

        PopulationGeneral = BBO.getPopulation();

        return  BestLocal;
    }


    private static Solution BSO(){
        //Solution BestLocal = BSO.CooperationExec(PopulationGeneral);
        return  new Solution();
    }


}
