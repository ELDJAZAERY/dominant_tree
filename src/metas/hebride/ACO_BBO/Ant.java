package metas.hebride.ACO_BBO;

import data.reader.Instances;
import data.representations.Solutions.Solution;
import data.representations.Vertex;
import metas.BBO.BBO;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.stream.Collectors.toMap;

public class Ant {

    private static HashMap<Integer,Double> pheromone_table;
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

    public void build_Solution() {
        if (Random() > ACO.q0 ) {
            build_par_Intensification();
        } else {
            build_par_diversification();
        }

        LocalSearch();
        if(solLocal.fitness < ACO.BestSol.fitness){
            ACO.BestSol = solLocal;
            solLocal.display();
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
        solLocal = metas.hebride.ACO_BBO.BBO.BBO_As_LocalSearch(solLocal);
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


}


