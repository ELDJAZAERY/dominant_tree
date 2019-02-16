package methas.VNS;

import data.representation.Node;
import data.representation.Solution;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class VNSolution extends Solution implements Comparable ,Cloneable {

    public VNSolution shaking(int K) {

        if(this.getDominoTree().size()<2) return this;

        VNSolution sol = (VNSolution) this.clone();
        int randomIndx = 0;

        for(int i=1 ; i <= K ; i++) {
            randomIndx = ThreadLocalRandom.current()
                    .nextInt(1, getDominoTree().size()-1);
            sol.getDominoTree().set(randomIndx,getDominoTree()
                                    .get(randomIndx-1)
                                    .getRandomNeighbor(getDominoTree())
                                );
        }

        return correction(sol);
    }

    public VNSolution correction(VNSolution sol){
        Node last , current ;
        ArrayList<Node> tempDominTree = new ArrayList<>();


        for(int i=1;i<sol.getDominoTree().size()-2;i++){
            last = sol.getDominoTree().get(i-1);
            current = sol.getDominoTree().get(i);

            tempDominTree.add(last);

            if(last.weight(current) == 0) {
                current = last.getRandomNeighbor(sol.getDominoTree());
                if(current == null)
                    current = last.getRandomNeighbor(tempDominTree);
                if(current == null)
                    current = data.representation.Graph.getRandomNode(sol.getDominoTree());
                if(current == null)
                    current = data.representation.Graph.getRandomNode(tempDominTree);
            }

            sol.getDominoTree().set(i,current);
        }

        sol.MAJ_Fitness();
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

    @Override
    protected Object clone() {
        return super.clone();
    }

    @Override
    public int compareTo(Object o) {
        return super.compareTo(o);
    }

}
