package methas.VNS;

import data.representation.Graph;
import data.representation.Node;
import data.representation.solutions.Solution;

public class VNSolution extends Solution implements Comparable ,Cloneable {


    public VNSolution(){}

    public VNSolution shaking(int K) {

        if(dominoTree.size() < 2) return this;

        VNSolution clone = (VNSolution) this.clone();

        Node oldNode , newNode;

        for(int i=0 ; i <= K ; i++) {
            oldNode = clone.getRandomNode();
            newNode = oldNode.getRandomNeighbor(clone.dominoTree);
            if(newNode != null){
                clone.dominoTree.remove(oldNode);
                clone.dominoTree.add(newNode);
            }
        }

        // Correction of solution after shaking
        clone.correction();

        return clone;
    }

    public VNSolution LocalSearch(int K){
        VNSolution sol = new VNSolution();
        for(int i = 2 ; i <= K ; i++){
            sol = shaking(i);
            if(compareTo(sol)>0) return sol;
        }
        return sol;
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