package data.representation;

import java.util.ArrayList;


public class Solution implements Comparable {

    private ArrayList<Node> dominoTree = new ArrayList<>();
    private double fitness;

    // @ getters
    public ArrayList<Node> getDominoTree(){
        return dominoTree;
    }


    public double fitness(){
        return fitness;
    }


    @Override
    public int compareTo(Object o) {
        return (int) (fitness - ((Solution)o).fitness);
    }

}
