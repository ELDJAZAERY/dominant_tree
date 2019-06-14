package metas.ACO;

import data.reader.Instances;
import data.representations.Solutions.Solution;
import data.representations.Vertex;
import metas.Controller;


import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.stream.Collectors.toMap;

public class Ant {

    protected static HashMap<Integer,Double> pheromone_table;
    private ArrayList<Integer> permutation = new ArrayList<>();

    private Solution solLocal;
    private int index ;


    Ant(int i){
        index = i % Instances.NbVertices;
    }

    static void Initials_Pheromones(){
        pheromone_table = new HashMap<>();
        for(int i = 0; i< Instances.NbVertices ; i++){
            pheromone_table.put(i,0.05);
        }
    }


    private float Random(){
        return ThreadLocalRandom.current().nextFloat();
    }


    //1(intens tourn ) 2(intens diver)3(diver tourn)
    public void build_Solution() {
        if (Random() > ACO.q0 ) {
            //  build_par_Intensification();
            //  build_par_diversification();
            builde_by_tournoi();
            // builde_by_ellitism();

        } else {
            build_par_diversification();
            //      builde_by_ellitism();
            //    builde_by_tournoi();
        }
        LocalSearch();
        if(solLocal.fitness < ACO.BestSol.fitness){
            ACO.BestSol = solLocal;
            Controller.majFitness(solLocal);
        }
    }


    private void build_par_Intensification(){
        permutation = new ArrayList<>();

        HashMap<Integer,Double> sorted = pheromone_table
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));

        for (Integer v:sorted.keySet()){
            permutation.add(v);
        }

        permute();
        solLocal = new Solution(permutation);
    }


    private void build_par_diversification(){
        if(permutation.isEmpty())
            build_par_Intensification();

        Collections.shuffle(permutation);
        permute();
        solLocal = new Solution(permutation);
    }

    private void LocalSearch(){

        int temp;
        Solution current ;
        Solution LocalBest = solLocal;
        ArrayList<Integer> permutationTemp;


        for (int d = 0; d < solLocal.verticesDT.size(); d++) {
            if(Controller.isStoped()) break;
            for (int v = solLocal.verticesDT.size(); v < Instances.NbVertices ; v++) {
                if(Controller.isStoped()) break;
                permutationTemp = new ArrayList<>(solLocal.permutation);
                temp = permutationTemp.get(d);
                permutationTemp.set(d, permutationTemp.get(v));
                permutationTemp.set(v, temp);

                current = new Solution(permutationTemp);

                if (current.fitness < solLocal.fitness) {
                    LocalBest = current;
                }
            }
            solLocal = LocalBest;
        }
    }


    protected void MAJ_OnLine(){
        double alpha ;
        for (Integer v : pheromone_table.keySet()) {
            alpha = solLocal.verticesDT.contains(new Vertex(""+v)) ? 0.05 : 0;
            pheromone_table.put(v, (ACO.raux * pheromone_table.get(v) + alpha));
        }
    }

    protected static void MAJ_OffLine(Solution best){
        double alpha ;
        for (Integer v : pheromone_table.keySet()) {
            alpha = best.verticesDT.contains(v) ? 0.05 : 0;
            pheromone_table.put(v, (ACO.raux * pheromone_table.get(v) + alpha));
        }
    }

    public void permute(){
        Integer temp;
        Integer temp1;
        temp=permutation.get(index);
        temp1=permutation.get(0);
        permutation.set(0,temp);
        permutation.set(index,temp1);
    }

    public void builde_by_ellitism(){
        permutation = new ArrayList<>();

        HashMap<Integer,Double> sorted = pheromone_table
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));
        int i=0;
        for(Integer v:sorted.keySet()){
            if(i<5){
                i++;
                permutation.add(v);
            }else break;
        }
        //mapShuffle
        List keys=new ArrayList(pheromone_table.keySet());
        Collections.shuffle(keys);
        for(Object o:keys){
            if(!permutation.contains(o)){
                permutation.add((Integer)o);
            }
        }
        solLocal = new Solution(permutation);

    }

    public void builde_by_tournoi(){
        permutation = new ArrayList<>();
        Random rand=new Random();
        int i = rand.nextInt(50);
        int j = rand.nextInt(50);
        List keys=new ArrayList(pheromone_table.keySet());
        Integer v1=(Integer)keys.get(i);
        Integer v2=(Integer)keys.get(j);
        if(pheromone_table.get(v1)>pheromone_table.get(v2)){
            permutation.add(v1);
        }else permutation.add(v2);
        HashMap<Integer,Double> sorted = pheromone_table
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));
        for(Integer o :sorted.keySet()){
            if(!permutation.contains(o)){
                permutation.add(o);
            }
        }

        solLocal = new Solution(permutation);
    }

    public void builde_by_tournoishuff(){
        permutation = new ArrayList<>();
        Random rand=new Random();
        int i = rand.nextInt(50);
        int j = rand.nextInt(50);
        List keys = new ArrayList(pheromone_table.keySet());
        Integer v1=(Integer)keys.get(i);
        Integer v2=(Integer)keys.get(j);
        if(pheromone_table.get(v1)>pheromone_table.get(v2)){
            permutation.add(v1);
        }else permutation.add(v2);
        HashMap<Integer,Double> sorted = pheromone_table
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));
        for(Integer o :pheromone_table.keySet()){
            if(!permutation.contains(o)){
                permutation.add(o);
            }
        }

        solLocal = new Solution(permutation);
    }

}


