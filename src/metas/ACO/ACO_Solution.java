package metas.ACO;

import data.reader.Instances;
import data.representations.Solutions.Solution;
import data.representations.Vertex;

import java.util.*;

import static java.util.stream.Collectors.summarizingLong;
import static java.util.stream.Collectors.toMap;

public class ACO_Solution implements Comparable {
    Solution sol,solF;
    Double raux=0.05;
    HashMap<Integer,Double>pheromon_table=new HashMap<>();


    public ACO_Solution(){
        // Random Solution
        sol = new Solution();
        // sol it's a valid Solution
        // connexe and dominate
        ArrayList<Integer>vertices=new ArrayList<>();
        for(int i=0;i< Instances.graph.vertices.size();i++){
            vertices.add(i);
        }
       vertices= shuffle(vertices);
        ACO_Solution var=new ACO_Solution(vertices);
    }

    public ACO_Solution(ArrayList<Integer> vertices){
        // Build Solution from List of Permutations
        // ex : < 15 , 20 , 3 , 41 , 25 , 6 , 7 , 8 , 9 , 24 , 29 >
        // PS : ordre important
        // ex < 15 , 20 ....   and  < 20 , 15 ... peuvant donne des diff solutions
        Integer p=0;
        while (p<vertices.size()){
            pheromon_table.put(vertices.get(p),0.05);
            p=p+1;
        }
        Solution tempSolution=new Solution(vertices);
        int i=0;
        solF=(Solution)tempSolution.clone();

        while (i<1000) {

            for(int ant=0;ant<50;ant++) {
                vertices=permute(vertices,i);
                    sol = new Solution(vertices);

                   if(sol.fitness - solF.fitness<0){
                       solF=(Solution)sol.clone();
                       System.out.println(solF.fitness);
                   }

                MAJ_pheromones(vertices);
                    HashMap<Integer,Double>sorted = pheromon_table
                            .entrySet()
                            .stream()
                            .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                            .collect(
                                    toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                            LinkedHashMap::new));
                    vertices.clear();
                    for (Integer node:sorted.keySet()){
                        vertices.add(node);
                    }
                    // sol it's a valid Solution
                    // connexe and dominate
            }
            Mise_Offline(vertices);
            HashMap<Integer,Double>sorted = pheromon_table
                    .entrySet()
                    .stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(
                            toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                    LinkedHashMap::new));
            vertices.clear();
            for (Integer node:sorted.keySet()){
                vertices.add(node);
            }
            i++;
        }
        System.out.println(solF.fitness);
        System.out.println(solF.path.toString());
    }


    /**
     *
     * EX : Compare two ACO Solution
     *
     *  (s1.compareTo(s2) < 0) ===> s1 better than s2
     *
     **/


    public void MAJ_pheromones(ArrayList<Integer> Nodes) {
        for (Integer n : Nodes) {
            //System.out.println(""+Nodes.size());
            //System.out.println(DominatesNodes.size());
            if (sol.verticesDT.contains(n)) {
                pheromon_table.put(n, (raux * pheromon_table.get(n) +0.05));
            } else {
                //     System.out.println("jhhhhhhhhhhhhhhhhhhh");
                //   System.out.println(""+phermon_table.get(n));
                pheromon_table.put(n, (raux * pheromon_table.get(n)));
                // System.out.println("phetpmo,"+phermon_table.size());
            }
        }
    }

    //
    public void Mise_Offline(ArrayList<Integer> Nodes) {

        for (Integer n : Nodes) {
            if (solF.verticesDT.contains(n)) {
                pheromon_table.put(n, (raux * pheromon_table.get(n) + 0.05));
            }
            else pheromon_table.put(n, (raux * pheromon_table.get(n)));

        }
    }

    public ArrayList<Integer> permute(ArrayList<Integer>vertices,int i){
        Integer temp;
        Integer temp1;
        temp=vertices.get(i);
        temp1=vertices.get(0);
        vertices.set(0,temp);
        vertices.set(i,temp1);
        return vertices;
    }

    @Override
    public int compareTo(Object o) {
        return  (int)sol.fitness - (int) ((Solution) o).fitness ;
    }

    private ArrayList<Integer> shuffle(ArrayList<Integer> list) {
        // get size of the list
        int totalElements = list.size();
        // initialize random number generator
        Random random = new Random();
        for (int loopCounter = 0; loopCounter < totalElements; loopCounter++) {
            // get the list element at current index
            Integer currentElement = list.get(loopCounter);
            // generate a random index within the range of list size
            int randomIndex = loopCounter + random.nextInt(totalElements - loopCounter);
            // set the element at current index with the element at random
            // generated index
            list.set(loopCounter, list.get(randomIndex));
            // set the element at random index with the element at current loop
            // index
            list.set(randomIndex, currentElement);
        }
        return list;
    }




    


}
