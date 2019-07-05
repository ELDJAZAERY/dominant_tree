package metas;

import data.representations.Solutions.Solution;
import metas.ACO.ACO;
import metas.BBO.BBO;
import metas.GA.GA;

import java.util.*;

import static java.util.stream.Collectors.toMap;


public class Coopertaion {

    public static String CurrentMeta = MetasEnum.BBO.name();


    private static HashMap<String , Double> MetaProbInitial;
    static{
        MetaProbInitial = new HashMap<>();
        MetaProbInitial.put(MetasEnum.BBO.name(),0.25);
        MetaProbInitial.put(MetasEnum.ACO.name(),0.25);
        MetaProbInitial.put(MetasEnum.GA.name() ,0.25);
    }

    private static ArrayList<Solution> PopulationGeneral ;
    private static Solution BestGeneral ;
    private static HashMap<String , Solution> BestByMeta = new HashMap<>();

    private static HashMap<String , Double> MetaProba ;
    private static ArrayList<String> listTAbou ;


    /** Cooperation @Params **/
    public static int populationSize = 20;
    public static int nbIterations = 1000;


    public static Solution Cooperate(){

        PopulationGeneral = GA.GA_ForInitializatoinPopulation(populationSize);
        listTAbou = new ArrayList<>();
        BestGeneral = new Solution();
        MetaProba = new HashMap<>(MetaProbInitial);

        Solution currentSol ;
        while(!Controller.isStopped()){
            MetaProba = MetaProba
                    .entrySet()
                    .stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(
                            toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                    LinkedHashMap::new));

            for(String methaName:MetaProba.keySet()){
                if(!listTAbou.contains(methaName) && (currentSol = MetasExec(methaName)).fitness < BestGeneral.fitness){
                    BestGeneral = currentSol;
                    Controller.majFitness(BestGeneral);
                    MAJProba(methaName);
                }
            }

            MAJProba();
        }

        return BestGeneral;
    }

    private static void MAJProba(){

        if(MetaProba.containsKey(MetasEnum.ACO.name()))
            if(MetaProba.get(MetasEnum.ACO.name()) <= 0 && MetaProba.keySet().size() > 1){
                MetaProba.remove(MetasEnum.ACO.name());
                listTAbou.add(MetasEnum.ACO.name());
            }
            else if(MetaProba.containsKey(MetasEnum.ACO.name()))
                MetaProbInitial.put(MetasEnum.ACO.name(),MetaProba.get(MetasEnum.ACO.name())-0.05);

        if(MetaProba.containsKey(MetasEnum.GA.name()))
            if(MetaProba.get(MetasEnum.GA.name()) <= 0 && MetaProba.keySet().size() > 1){
                MetaProba.remove(MetasEnum.GA.name());
                listTAbou.add(MetasEnum.GA.name());
            }
            else if(MetaProba.containsKey(MetasEnum.ACO.name()))
                MetaProbInitial.put(MetasEnum.GA.name(),MetaProba.get(MetasEnum.GA.name())-0.05);


        if(MetaProba.containsKey(MetasEnum.BBO.name()))
            if(MetaProba.get(MetasEnum.BBO.name()) <= 0 && MetaProba.keySet().size() > 1){
                MetaProba.remove(MetasEnum.BBO.name());
            }
            else if(MetaProba.containsKey(MetasEnum.ACO.name()))
                MetaProbInitial.put(MetasEnum.BBO.name(),MetaProba.get(MetasEnum.BBO.name())-0.05);
    }

    private static void MAJProba(String meta){
        MetaProba.put(meta,MetaProba.get(meta) + 0.05);
        listTAbou.clear();
    }

    private static Solution MetasExec(String meta){
        switch (meta){
            case "ACO" :
                /** ACO **/
                return ACO();
            case "BBO" :
                /** BBO **/
                return BBO();
            case "GA" :
                /**  GA **/
                return GA();
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

    private static Solution GA(){
        Solution BestLocal = GA.Exec(5,PopulationGeneral);
        return  BestLocal;
    }






    // @SuppressWarnings("unused")
    private static Solution BSO(){
        //Solution BestLocal = BSO.CooperationExec(PopulationGeneral);
        return  new Solution();
    }


    // @SuppressWarnings("unused")
    public static void cooper(){
        ACO.ACO_Exec();
    }

}
