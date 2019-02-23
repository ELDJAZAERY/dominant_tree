package methas.defaultMetha;

import data.representation.*;



public class dSolution extends Solution implements Comparable ,Cloneable {


    public dSolution(){}

    public dSolution shaking(int K) {

        if(dominoTree.size() < 2) return this;

        dSolution clone = (dSolution) this.clone();

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

    public dSolution LocalSearch(int K){
        if(dominoTree.size() < 2) return this;

        dSolution cloneSolution = (dSolution) this.clone();

        Node oldNode , newNode;

        for(int i=0 ; i <= K ; i++) {
            oldNode = cloneSolution.getRandomNode();
            newNode = oldNode.getRandomNeighbor(cloneSolution.dominoTree);
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
