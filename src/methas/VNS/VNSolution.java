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
    public VNSolution(VNSolution sol){
        VNSolution cloned = (VNSolution) sol.clone();

        Collections.shuffle(cloned.getDominoTree());
        cloned = correction(cloned);
        getDominoTree().addAll(cloned.getDominoTree());
        setFitness(cloned.fitness());
        solution = cloned.solution;
    }

    public VNSolution shaking(int K) {

        if(this.getDominoTree().size()<2) return this;

        VNSolution sol = (VNSolution) this.clone();

        int randomIndex;
        Node randNode;

        for(int i=1 ; i <= K ; i++) {
            randomIndex = getRandomIndex(1,getDominoTree().size()-1);
            randNode = getDominoTree().get(randomIndex-1).getRandomNeighbor(getDominoTree());
            if(randNode != null){
                getDominoTree().set(randomIndex,randNode);
            }
        }

        sol = correction(sol);
        sol.MAJ_sol();

        return sol;
    }




    public static VNSolution correction(VNSolution sol){

        // already solution
        if(Graph.isDomiTree(sol.getDominoTree()))
            return sol;

        Node current , next ;
        LinkedList<Node> dominTree = new LinkedList<>();


        // escape an infinity loop when pop out dominate tree
        int faildNodesCachMemoryTimer = 0;
        ArrayList<Node> failedNode = new ArrayList<>();


        dominTree.add(sol.getDominoTree().get(0));

        for(int i = 1 ; i < sol.cardinal()-1 ; i++) {
            current = sol.getDominoTree().get(i-1);
            next    = sol.getDominoTree().get(i);

            if(current.isNeighbor(next)) dominTree.add(next);
            else {
                next = sol.getNextNode(current,failedNode);
                while(next == null){
                    dominTree.remove(current);
                    if(dominTree.size() == 0) { return new VNSolution();}
                    current = dominTree.pop();
                    failedNode.add(current);
                    faildNodesCachMemoryTimer++;
                    next = sol.getNextNode(current,failedNode);
                }
                dominTree.add(next);
            }


            if(faildNodesCachMemoryTimer < 0){
                failedNode.clear();
            }else faildNodesCachMemoryTimer--;

            // @ repeated
            // TODO see this
            if(Graph.isDomiTree(sol.getDominoTree()))
                return sol;
        }

        if(Graph.isDomiTree(sol.getDominoTree()))
            return sol;

        return correctionPerAddNodes(sol);
    }


    public static VNSolution correctionPerAddNodes(VNSolution sol){
        // TODO @ correction per add Nodes
        return new VNSolution();
    }


/*
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

*/

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
