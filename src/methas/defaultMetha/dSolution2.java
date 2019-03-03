package methas.defaultMetha;

import data.representation.Graph;
import data.representation.Node;
import data.representation.solutions.Binary_Solution;


public class dSolution2 extends Binary_Solution implements Comparable ,Cloneable {


    public dSolution2(){}

    public dSolution2 shaking(int K) {

        if(dominoTree.size() < 2) return this;

        dSolution2 clone = (dSolution2) this.clone();

        Node oldNode ;

        for(int i=0 ; i <= K ; i++) {
            if(clone.size() < 2) break;
            // Remove K random Nodes
            oldNode = clone.getRandomNode();
            clone.getDominoTree().remove(oldNode);
        }

        // Correction of solution after shaking
        clone.correction();

        return clone;
    }

    public dSolution2 LocalSearch(int K){
        if(dominoTree.size() < 2) return this;

        dSolution2 cloneSolution = (dSolution2) this.clone();

        Node oldNode , newNode;

        for(int i=1 ; i <= K ; i++) {
            oldNode = cloneSolution.getRandomNode();
            newNode = Graph.getRandomNode();
            if(newNode != null){
                cloneSolution.dominoTree.remove(oldNode);
                cloneSolution.dominoTree.add(newNode);
            }
        }

        // Correction of solution after shaking
        cloneSolution.correction();

        return cloneSolution;
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
