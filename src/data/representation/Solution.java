package data.representation;

import methas.VNS.VNSolution;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


public class Solution implements Comparable , Cloneable  {

    protected HashSet<Node> dominoTree ;
    private HashSet<Arc> path = new HashSet<>();
    private double fitness;
    public boolean isSolution;
    private Set<Node> ExploredNode ;


    // @ Generate Default Random Solution
    public Solution(){

        // Nodes initial
        Node CurrentNode ;
        Arc  CurrentArc;
        isSolution = false;

        // while we don't find solution and there is more Nodes
        ExploredNode = new HashSet<>();
        dominoTree   = new HashSet<>();
        if(Graph.DominatesNodes.size() != 0){
            dominoTree.addAll(Graph.DominatesNodes);
        }else{
            dominoTree.add(Graph.getRandomNode());
        }

        while(true){

            CurrentNode  = Graph.getRandomNeighborNode(dominoTree);

            /* ### explore all Node without find Solution ### */
            if(CurrentNode == null) {
                System.err.println("### No Solution -- Graph Non Connexe ###");
                System.exit(-1);
            }

            dominoTree.add(CurrentNode);
            // TODO CurrentNode.get -- Random Arc Neighbor --
            CurrentArc   = CurrentNode.getMinArcNeighbor(dominoTree);
            path.add(CurrentArc);

            ExploredNode.add(CurrentNode);
            ExploredNode.addAll(CurrentNode.getNeighborsNodes());

            if(Graph.isExplored(ExploredNode)){
                MAJ_Fitness();
                isSolution = true;
                return;
            }
        }
    }



    // @ getters
    public HashSet<Node> getDominoTree(){
        return dominoTree;
    }

    public double fitness(){
        return fitness;
    }

    public int size(){
        return dominoTree.size();
    }

    public Node get(int index){

        if(index < 0 || index >= dominoTree.size())
            return null;
        int i = 0;
        for(Node n:dominoTree){
            if(i == index)
                return n;
            i++;
        }
        return null;
    }



    // Random Getters

    public Node getRandomNode(){
        int randindex = ThreadLocalRandom.current()
                .nextInt(0,dominoTree.size());

        return get(randindex);
    }



    // Correction Phase

    // TODO NO CORRECTION YET
    public void correction(){

        // already solution
        //if(Graph.isDomiTree(this)) return;

        isSolution = false;

        HashSet<Node> tempNodes = new HashSet<>();

        // NO Connexe Graph
        //tempNode.addAll(Graph.DominatesNodes);


        Node CurrentNode;
        CurrentNode =  get(0);
        tempNodes.add(CurrentNode);

        Arc CurrentArc;
        this.path.clear();
        fitness = 0;

        for(Node n:dominoTree){
            if(n.isNeighbor(tempNodes)){
                tempNodes.add(n);
                // TODO n.get -- Random Arc Neighbor --
                CurrentArc   = n.getMinArcNeighbor(tempNodes);
                path.add(CurrentArc);
                fitness += CurrentArc.getWeight();
            }
            if(Graph.isExplored(tempNodes) && isConnexe()) {
                dominoTree = tempNodes;
                MAJ_Fitness();
                return;
            }
        }

        dominoTree.clear();

        while(!Graph.isExplored(tempNodes)){
            CurrentNode = Graph.getRandomNeighborNode(tempNodes);
            tempNodes.add(CurrentNode);
            // TODO n.get -- Random Arc Neighbor --
            CurrentArc   = CurrentNode.getMinArcNeighbor(tempNodes);
            path.add(CurrentArc);
            fitness += CurrentArc.getWeight();
        }

        dominoTree = tempNodes;
    }


    public void MAJ_sol(){
        // connect dominate Node
        Connect();

        // TODO MAJ ARCs
        MAJ_Arcs();

        // MAJ Fitness
        MAJ_Fitness();
    }


    public void MAJ_Arcs(){

    }

    public void MAJ_Fitness(){
        fitness = 0;
        if(dominoTree.size() == 0 || path.size() == 0) return;
        for(Arc arc:path){
            fitness += arc.getWeight();
        }
    }

    public void Connect(){
        while(!isConnexe()){
            dominoTree.add(Graph.getRandomNeighborNode(dominoTree));
        }
    }


    public boolean isConnexe(){
        ArrayList<Node> nodes = new ArrayList<>(dominoTree);
        HashSet<Node> tempTree = new HashSet<>();

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


    @Override
    public int compareTo(Object o) {
        return (int)(fitness - ((Solution) o).fitness);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Solution)) return false;
        return dominoTree.equals(((Solution) o).dominoTree);
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
            clone.dominoTree = new HashSet<>(dominoTree);
            clone.fitness = fitness;
            return clone;
        }catch (Exception e){
            System.err.println("\n\nSolution can't be cloned : CloneNotSupportedException Exception");
            return this;
        }
    }

}
