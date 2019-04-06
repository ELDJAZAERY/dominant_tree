package metas.ACO;

import data.representations.Solutions.Solution;

import java.util.ArrayList;

public class ACO_Solution implements Comparable {

    Solution sol;

    public ACO_Solution(){
        // Random Solution
        sol = new Solution();
        // sol it's a valid Solution
        // connexe and dominate
    }

    public ACO_Solution(ArrayList<Integer> vertices){
        // Build Solution from List of Permutations
        // ex : < 15 , 20 , 3 , 41 , 25 , 6 , 7 , 8 , 9 , 24 , 29 >
        // PS : ordre important
        // ex < 15 , 20 ....   and  < 20 , 15 ... peuvant donne des diff solutions
        sol = new Solution(vertices);
        // sol it's a valid Solution
        // connexe and dominate
    }


    /**
     *
     * EX : Compare two ACO Solution
     *
     *  (s1.compareTo(s2) < 0) ===> s1 better than s2
     *
     **/


    @Override
    public int compareTo(Object o) {
        return  (int)sol.fitness - (int) ((Solution) o).fitness ;
    }
}
