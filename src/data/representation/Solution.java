package data.representation;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


public class Solution implements Comparable , Cloneable  {

    private ArrayList<Node> dominoTree = new ArrayList<>();
    private double fitness;
    private boolean solution = false;


    // @ Generate Default Random Solution
    public Solution(){

        Set<Node> exploredNodes = new HashSet<>();
        Node currentNode , nextNode , tempNode ;
        boolean stop = false;

        currentNode = Graph.getRandomNode();
        if(currentNode == null){
            System.err.println(" ----- No Default Solution ------");
            return;
        }

        int itter = 0 , maxIteration = 50 ;

        while(!stop){
            // for each Node choose one random of her neighbors
            while(currentNode != null && !Graph.isExplored(exploredNodes) ){
                dominoTree.add(currentNode);
                nextNode = currentNode.getRandomNeighbor(dominoTree);
                fitness += currentNode.weight(nextNode);
                exploredNodes.addAll(currentNode.getNeighborsNodes());
                currentNode = nextNode;
            }

            // It is solution
            if(Graph.isExplored(exploredNodes)){
                solution = true;
                break;
            }


            // else : so we have an current Node who haven't
            //        any more neighbor not in our dominTree
            //        so we should change last node in our domTree
            // ## <Correction phase>
            if(dominoTree.size() < 2) return;
            Set<Node> back = new HashSet<>();
            back.add(dominoTree.get(dominoTree.size()-1));
            for( int i = dominoTree.size()-2 ; i >=0 ;i--){
                tempNode = dominoTree.get(i);
                currentNode = tempNode.getRandomNeighbor(dominoTree);
                if(currentNode != null) break;
                back.add(tempNode);
            }

            if(currentNode == null)
                currentNode = Graph.getRandomNode(exploredNodes);
            if(currentNode == null)
                currentNode = Graph.getRandomNode(dominoTree);
            if(currentNode == null){
                System.err.println("---- Default Solution : erreur ---");
                stop = true;
            }

            exploredNodes.clear();
            dominoTree.removeAll(back);
            fitness = 0;
            tempNode = dominoTree.get(0);
            for(Node n:dominoTree){
                fitness += tempNode.weight(n);
                tempNode = n;
                exploredNodes.addAll(n.getNeighborsNodes());
            }

            // ## </Correction phase>




            // infinity loop control
            itter++;
            if(itter == maxIteration)
                System.err.println(" ------- Default Solution : Max iteration overflow ------ ");
        }


    }


    // @ getters
    public ArrayList<Node> getDominoTree(){
        return dominoTree;
    }

    public double fitness(){
        return fitness;
    }

    public boolean isSolution() {
        return solution;
    }

    public void MAJ_Fitness(){
        fitness = 0;
        Node tempNode = dominoTree.get(0);
        for(Node n:dominoTree){
            fitness += tempNode.weight(n);
            tempNode = n;
        }
    }

    public void setFitness(double fitness){
        this.fitness = fitness;
    }

    @Override
    public int compareTo(Object o) {
        return (int)(fitness - ((Solution) o).fitness);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Solution)) return false;

        Solution s = (Solution) o;
        if(dominoTree.size() != s.dominoTree.size())
            return false;

        for(int i = 0 ; i < dominoTree.size();i++ ){
            if (!(dominoTree.get(i).equals(s.dominoTree.get(i))))
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dominoTree);
    }


    @Override
    public String toString() {
        String out = "";
        out += "\n\nSolution { \n " ;
        out += "fitness = " + fitness + " ,\n";
        out += "Cardinality = " + dominoTree.size() + " ,\n";
        out += "Nodes = [\n";
        for(Node n:dominoTree){
            out += "@" + n.getName() + "\n";
        }
        out += "]\n }";

        return out;
    }


    @Override
    protected Object clone() {
        Solution clone = null;
        try{
            clone = (Solution) super.clone();
            clone.dominoTree = new ArrayList(dominoTree);
            clone.fitness = fitness;
            clone.solution = solution;
            return clone;
        }catch (Exception e){
            System.err.println("\n\nSolution can't be cloned : CloneNotSupportedException Exception");
            return this;
        }
    }

}
