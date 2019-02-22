package methas.VNS;

import data.representation.Graph;
import data.representation.Node;
import data.representation.Solution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

public class VNSolution extends Solution implements Comparable ,Cloneable {


    public VNSolution(){}

    public VNSolution shaking(int K) {

        if(dominoTree.size() < 2) return this;

        VNSolution sol = (VNSolution) this.clone();

        Node oldNode , newNode;

        for(int i=1 ; i <= K ; i++) {
            oldNode = sol.getRandomNode();
            newNode = oldNode.getRandomNeighbor(dominoTree);
            if(newNode != null){
                sol.dominoTree.remove(oldNode);
                sol.dominoTree.add(newNode);
            }
        }

        // Correction of solution after shaking
        sol.correction();

        return sol;
    }


    public  VNSolution LocalSearch(int K){
        VNSolution sol ;
        for(int i = 2 ; i <= K ; i++){
            sol = shaking(i);
            if(compareTo(sol)>0) return sol;
        }
        return this;
    }

    public VNSolution DeepLocalSearch(int K){
        VNSolution sol , BestSol = this;
        for(int i = 2 ; i <= K ; i++){
            sol = shaking(i);
            if(BestSol.compareTo(sol)>0) BestSol = sol;
        }
        return BestSol;
    }


    @Override
    protected Object clone() {
        return super.clone();
    }

    @Override
    public int compareTo(Object o) {
        return super.compareTo(o);
    }

}
