package data.representation;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


public class Solution implements Comparable , Cloneable  {

    private LinkedList<Node> dominoTree = new LinkedList<>();
    private double fitness;
    protected boolean solution = false;

    private Set<Node> ExploredNode = new HashSet<>();

    // @ Generate Default Random Solution



    public Solution(){

        // Nodes initial
        Node  NextNode , tempNode , CurrentNode = null;


        // while we don't find solution and there is more Nodes
        ExploredNode = new HashSet<>();
        dominoTree   = new LinkedList<>();
        dominoTree.add(Graph.getRandomNode());

        while(true){

            // TODO Arraylist not linkedlist
            CurrentNode  = Graph.getRandomNeighborNode(new ArrayList<>(dominoTree));
            /* ### explore all Node without find Solution ### */
            if(CurrentNode == null) {
                System.err.println("### No Solution -- Graph Non Connexe ###");
                System.exit(-1);
            }

            ExploredNode.add(CurrentNode);
            dominoTree.add(CurrentNode);
            ExploredNode.addAll(CurrentNode.getNeighborsNodes());


            if(Graph.isExplored(ExploredNode)){
                solution = true;
                MAJ_Fitness();
                return;
            }

        }

    }





/*

    public Solution(){

        // initialisation phase
        Set<Node> nodes_init = new HashSet<>();

        // Nodes initial
        Node  NextNode , tempNode , CurrentNode = null;


        // escape an infinity loop when pop out dominate tree
        int faildNodesCachMemoryTimer = 0;
        ArrayList<Node> failedNode = new ArrayList<>();


        // while we don't find solution and there is more Nodes
        // in graph to start with DO
        while(!nodes_init.containsAll(Graph.Nodes)){

            if(CurrentNode == null){
                // re-Initialisation phase
                ExploredNode = new HashSet<>();
                dominoTree   = new LinkedList<>();
                CurrentNode  = Graph.getRandomNode(nodes_init);
                */
/* ### explore all Node without find Solution ### *//*

                if(CurrentNode == null) {
                    System.err.println("### Can't find Solution ###");
                    System.exit(-1);
                }
                nodes_init.add(CurrentNode);
            }

            ExploredNode.add(CurrentNode);
            dominoTree.add(CurrentNode);


            // Check if next Node selected is available or not
            NextNode = getNextNode(CurrentNode,failedNode);

            while(NextNode == null ){
                dominoTree.remove(CurrentNode);
                if(dominoTree.size() == 0) { CurrentNode = null ; continue;}
                CurrentNode = dominoTree.pop();
                failedNode.add(CurrentNode);
                faildNodesCachMemoryTimer++;
                // TODO check Complexity Cost
                MAJ_NodeExplored();

                NextNode = getNextNode(CurrentNode,failedNode);

                // Dominate tree back empty
                if(CurrentNode == null) break;
            }

            // Dominate tree back empty
            if(CurrentNode == null) continue;

            // TODO Graph.isDominate tree in place of explored
            ExploredNode.addAll(CurrentNode.getNeighborsNodes());
            if(Graph.isExplored(ExploredNode)){
                solution = true;
                MAJ_Fitness();
                return;
            }

            CurrentNode = NextNode;

            if(faildNodesCachMemoryTimer < 0){
                failedNode.clear();
            }else faildNodesCachMemoryTimer--;

        }

    }

*/

    public Node getNextNode(Node CurrentNode , ArrayList<Node> faildNodes){
        if(CurrentNode == null) return null;

        ArrayList<Node> neighbors = (ArrayList<Node>) CurrentNode.getNeighborsNodes().clone();
        if(neighbors.isEmpty()) return null;

        Node neighbor ;
        int randomIndex ;

        while(neighbors.size() > 1){
            randomIndex = ThreadLocalRandom.current()
                    .nextInt(0, neighbors.size()-1);
            neighbor = neighbors.get(randomIndex);
            if( !dominoTree.contains(neighbor) && !faildNodes.contains(neighbor))
                return neighbor;

            neighbors.remove(randomIndex);
        }

        neighbor = neighbors.get(0);
        if( !dominoTree.contains(neighbor) && !faildNodes.contains(neighbor))
            return neighbor;


        return null;
    }


/*
    public Solution(){

        Set<Node> exploredNodes = new HashSet<>();
        Node currentNode , nextNode , tempNode ;
        boolean stop = false;

        currentNode = Graph.getRandomNode();
        if(currentNode == null){
            System.err.println(" ----- No Default Solution ------");
            return;
        }

        int itter = 0 , maxIteration = 500 ;

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
            if(itter == maxIteration){
                //System.err.println(" ------- Default Solution : Max iteration overflow ------ ");
            }
        }


    }

*/


    // @ getters
    public LinkedList<Node> getDominoTree(){
        return dominoTree;
    }

    public double fitness(){
        return fitness;
    }

    public boolean isSolution() {
        return solution;
    }

    public void correction(){
        while(!isConnexe()){
            dominoTree.add(Graph.getRandomNeighborNode(new ArrayList<>(dominoTree)));
        }
    }



    public boolean isConnexe(){
        ArrayList<Node> nodes = new ArrayList<>(dominoTree);
        ArrayList<Node> tempTree = new ArrayList<>();

        tempTree.add(nodes.get(0));
        nodes.remove(0);

        for(int i=0;i<nodes.size();i++){
            if(nodes.get(i).isNeighbor(tempTree)){
                tempTree.add(nodes.get(i));
                nodes.remove(i);
                i=-1;
            }
        }

        return nodes.isEmpty();
    }


    public void setFitness(double fitness){
        this.fitness = fitness;
    }

    public int cardinal(){
        return dominoTree.size();
    }

    public static int getRandomIndex(int bound1 , int bound2){
        if(bound1 >= bound2) return 0;
        return ThreadLocalRandom.current()
                .nextInt(bound1, bound2);
    }


    public void setDominoTree(LinkedList<Node> dominoTree){
        this.dominoTree = new LinkedList<>(dominoTree);
    }

    public void setDominoTree(ArrayList<Node> dominoTree){
        this.dominoTree = new LinkedList<>(dominoTree);
    }

    public void MAJ_sol(){
        ExploredNode = new HashSet<>();
        LinkedList<Node> tempDominTree = new LinkedList<>();

        fitness = 0;
        solution = false;
        if(dominoTree.size() == 0) return;
        Node tempNode = dominoTree.get(0);
        for(Node n:dominoTree){
            // TODO check complexity cost
            tempDominTree.add(n);
            if(Graph.isDomiTree(tempDominTree)){
                dominoTree = tempDominTree;
                return;
            }
            ExploredNode.addAll(n.getNeighborsNodes());
            fitness += tempNode.weight(n);
            tempNode = n;
        }
    }


    public void MAJ_Fitness(){
        fitness = 0;
        if(dominoTree.size() == 0) return;
        Node tempNode = dominoTree.get(0);
        for(Node n:dominoTree){
            fitness += tempNode.weight(n);
            tempNode = n;
        }
    }

    public void MAJ_NodeExplored(){
        ExploredNode = new HashSet<>();
        for(Node n:dominoTree){
            ExploredNode.addAll(n.getNeighborsNodes());
        }
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
            clone.dominoTree = new LinkedList(dominoTree);
            clone.fitness = fitness;
            clone.solution = solution;
            return clone;
        }catch (Exception e){
            System.err.println("\n\nSolution can't be cloned : CloneNotSupportedException Exception");
            return this;
        }
    }

}
