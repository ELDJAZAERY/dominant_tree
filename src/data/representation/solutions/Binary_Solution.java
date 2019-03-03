package data.representation.solutions;

import application.Main;
import data.representation.Arc;
import data.representation.Graph;
import data.representation.Node;
import sun.management.snmp.jvminstr.JvmThreadInstanceEntryImpl;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


public class Binary_Solution implements Comparable , Cloneable  {


    public static int nbEvaluations = 0;

    protected ArrayList<Node> dominoTree ;
    protected Byte[] binary ;

    private HashSet<Arc> path ;
    private double fitness;


    // @ Generate Default Random dSolution

    public Binary_Solution(){

        // while we don't find solution and there is more Nodes
        dominoTree   = new ArrayList<>();
        binary = new Byte[Graph.Nodes.size()];

        ArrayList<Node> graph = new ArrayList<>(Graph.Nodes);
        Collections.shuffle(graph);

        HashSet<Node> NodesExplored = new HashSet<>();

        if(Graph.DominatesNodes.size() != 0) {
            for(Node node:Graph.DominatesNodes){
                dominoTree.add(node);

                NodesExplored.add(node);
                NodesExplored.addAll(node.getNeighborsNodes());
            }
        }

        for(int i = 0 ; i < graph.size() ; i++){
            if(isSolution()) break;
            dominoTree.add(graph.get(i));
        }

        MAJ_sol();
        MAJ_binary();
    }


    public void MAJ_binary(){
        int i = 0;
        for(Node node:Graph.Nodes){
                binary[i] = (byte) 1;
                binary[i] = dominoTree.contains(node) ? (byte) 1 : (byte) 0;
                i++;
        }
    }


    // @ getters
    public ArrayList<Node> getDominoTree(){
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

    public HashSet<Arc> getPath() {
        return path;
    }



    // Random Getters

    public Node getRandomNode(){
        int randindex = ThreadLocalRandom.current()
                .nextInt(0,dominoTree.size());

        return get(randindex);
    }


    public void pruning(){
        ArrayList<Node> afterPruning = new ArrayList<>(dominoTree);
        for(Node node : dominoTree){
            if(node.isPurninable(afterPruning))
                afterPruning.remove(node);
        }
        dominoTree = afterPruning;
    }


    // TODO #Correction_Phase

    public boolean isSolution(){
        Set<Node> exploredNodes = new HashSet<>();
        for(Node n:dominoTree){
            exploredNodes.addAll(n.getNeighborsNodes());
        }
        return exploredNodes.containsAll(Graph.Nodes);
    }

    public void correctionBinary(){
        dominoTree.clear();

        int i = 0;
        for(Node node:Graph.Nodes){
            if(binary[i] == 1)
                dominoTree.add(node);
            i++;
        }

        correction();
    }

    public void correction(){

        ArrayList<Node> graph = new ArrayList<>(Graph.Nodes);
        Collections.shuffle(graph);

        ArrayList<Node> dominoTree = new ArrayList<>(this.dominoTree);

        // HashSet no duplicated Node
        this.dominoTree.clear();

        for(int i = 0 ; i < dominoTree.size() ; i++){
            if(isSolution()) {
                MAJ_sol();
                return;
            }

            this.dominoTree.add(dominoTree.get(i));
        }

        for(int i = 0 ; i < graph.size() ; i++){
            if(isSolution()) {
                MAJ_sol();
                return;
            }

            this.dominoTree.add(graph.get(i));
        }
    }


    public void MAJ_sol(){

        Connect();

        pruning();

        Connect();

        MST();

        MAJ_Fitness();
    }


    public void MST(){

        path = new HashSet<>();

        Node nextNode;
        ArrayList<Node> nodes = new ArrayList<>();
        ArrayList<Arc>  ququeArcs = new ArrayList<>();

        nextNode = get(0);
        nodes.add(nextNode);
        ququeArcs.addAll(nextNode.getArcs());

        while(!nodes.containsAll(dominoTree)){
            //System.out.println("\n\n ### BEFOR ### \n"+ququeArcs.toString()+" \n\n");
            Collections.sort(ququeArcs);
            //System.out.println("\n\n ### AFTER ### \n"+ququeArcs.toString()+" \n\n");
            for(Arc arc:ququeArcs) {
                if(!arc.appartien(dominoTree)) continue;
                if (!nodes.contains(arc.fin)){
                    path.add(arc);
                    nodes.add(arc.fin);
                    ququeArcs.addAll(arc.fin.getArcs());
                    break;
                }else if(!nodes.contains(arc.debut)){
                    path.add(arc);
                    nodes.add(arc.debut);
                    ququeArcs.addAll(arc.debut.getArcs());
                    break;
                }
            }
        }
    }


    public void MAJ_Fitness(){
        fitness = 0;
        if(dominoTree.size() == 0 || path.size() == 0) return;
        for(Arc arc:path){
            fitness += arc.getWeight();
        }
        nbEvaluations++;
    }


    public void Connect(){

        ArrayList<Node> DominateSet = new ArrayList<>(dominoTree);
        HashSet<Node> newDominateSet = new HashSet<>();

        HashSet<Node> path0 , path1 , path2 ;


        Node maxNode = getHaveMaxDominNeighbr(DominateSet);
        path0 = maxNode.getDominNeighbors(DominateSet);

        newDominateSet.add(maxNode);
        newDominateSet.addAll(path0);

        DominateSet.remove(maxNode);
        DominateSet.removeAll(path0);


        while(!DominateSet.isEmpty()){

            maxNode = getHaveMaxDominNeighbr(DominateSet);

            path0 = getPath0(newDominateSet,DominateSet,maxNode,false);

            if(!path0.isEmpty()){
                newDominateSet.addAll(path0);
                DominateSet.removeAll(path0);
                continue;
            }


            path1 = getPath1(newDominateSet,DominateSet,maxNode,false);

            if(!path1.isEmpty()){
                newDominateSet.addAll(path1);
                DominateSet.removeAll(path1);
                continue;
            }



            path2 = getPath2(newDominateSet,DominateSet,maxNode,false);

            if(!path2.isEmpty()){
                newDominateSet.addAll(path2);
                DominateSet.removeAll(path2);
                continue;
            }

        }

        dominoTree = new ArrayList<>(newDominateSet);
    }


    public HashSet<Node> getPath0(HashSet<Node> newDominateSet , ArrayList<Node> DominateSet , Node MaxNode , boolean onlyForMAxNode){

        HashSet<Node> path0 = new HashSet<>();

        HashSet<Node>  MaxNodeDN  , tempNodeDN ;

        if(MaxNode != null && MaxNode.isNeighbor(newDominateSet)){
            MaxNodeDN = MaxNode.getDominNeighbors(DominateSet);

            path0.add(MaxNode);
            path0.addAll(MaxNodeDN);

            return path0;
        }

        if(onlyForMAxNode) return path0;

        for(Node node:DominateSet){
            if(node.isNeighbor(newDominateSet)){
                tempNodeDN = node.getDominNeighbors(DominateSet);

                path0.add(node);
                path0.addAll(tempNodeDN);

                return path0;
            }
        }

        return path0;
    }


    public HashSet<Node> getPath1(HashSet<Node> newDominateSet , ArrayList<Node> DominateSet , Node MaxNode ,boolean onlyForMAxNode){

        HashSet<Node> path1 = new HashSet<>();

        if(MaxNode != null){
            for(Node node :MaxNode.getNeighborsNodes()){
                path1 = getPath0(newDominateSet,DominateSet,node,true);
                if(!path1.isEmpty()) {
                    path1.add(MaxNode);
                    path1.addAll(MaxNode.getDominNeighbors(DominateSet));
                    return path1;
                }
            }
        }

        if(onlyForMAxNode) return path1;


        for(Node node:DominateSet){
            for(Node node2 : node.getNeighborsNodes()){
                path1 = getPath0(newDominateSet,DominateSet,node2,true);
                if(!path1.isEmpty()) {
                    path1.add(node);
                    path1.addAll(node.getDominNeighbors(DominateSet));
                    return path1;
                }
            }
        }

        return path1;
    }


    public HashSet<Node> getPath2(HashSet<Node> newDominateSet , ArrayList<Node> DominateSet ,Node MaxNode,boolean onlyForMAxNode){

        HashSet<Node> path2 = new HashSet<>();

        if(MaxNode != null){
            for(Node node :MaxNode.getNeighborsNodes()){
                path2 = getPath0(newDominateSet,DominateSet,node,true);
                if(!path2.isEmpty()) {
                    path2.add(MaxNode);
                    path2.addAll(MaxNode.getDominNeighbors(DominateSet));
                    return path2;
                }
            }
        }

        if(onlyForMAxNode) return path2;


        for(Node node:DominateSet){
            for(Node node2 : node.getNeighborsNodes()){
                path2 = getPath1(newDominateSet,DominateSet,node2,true);
                if(!path2.isEmpty()) {
                    path2.add(node);
                    path2.addAll(node.getDominNeighbors(DominateSet));
                    return path2;
                }
            }
        }

        return path2;
    }


    // TODO
    public Node getHaveMaxDominNeighbr(ArrayList<Node> dominSet){

        int max = 0;
        Node maxNode = dominSet.get(0);

        for(Node node:dominSet){
            int nbDN = node.getDominNeighbors(dominSet).size();
            if( nbDN > max){
                maxNode = node;
                max = nbDN;
            }
        }

        return maxNode;
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
        return (int)(fitness - ((Binary_Solution) o).fitness);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Binary_Solution)) return false;
        return dominoTree.equals(((Binary_Solution) o).dominoTree);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dominoTree);
    }


    @Override
    public String toString() {
        String out = "";
        out += "\n\ndSolution { \n " ;
        out += "fitness = " + fitness + " ,\n";
        out += "Cardinality = " + dominoTree.size() + " ,\n";
        out += "Nodes = [\n";
        for(Node n:dominoTree){
            out += "@" + n.getName() + "\n";
        }
        out += "]\n }";

        return out;
    }


    public void printPerformance() {

        double now = System.currentTimeMillis() / 1000;

        String out = "\n";
        out += "Solution { \n " ;
        out += "\tfitness     : " + fitness   + " ,\n";
        out += "\tTime        : " + (now - Main.startTime) + " Sec ,\n";
        out += "\tCardinality : " + dominoTree.size() + " ,\n";
        out += "\tEvaluations : " + nbEvaluations + " times ,\n";
        out += "\tIsDominate  : " + isSolution() + " , \n";
        out += "\tIsConnexe   : " + isConnexe() + "  ,\n";
        out += "\tNb Arcs     : " + path.size() + "  ,\n";
        //out += "\tBinary      : " + binarytoString() + "  ,\n";
        //out += "\tArcs        : " + path.toString() + "  ,\n";

        out += "}\n";
        
        System.out.println(out);
    }

    public String binarytoString(){
        String out = " ";
        for(Byte bin:binary){
            out += bin + ",";
        }
        return out;
    }


    @Override
    protected Object clone() {
        Binary_Solution clone = null;
        try{
            clone = (Binary_Solution) super.clone();
            clone.dominoTree = new ArrayList<>(dominoTree);
            clone.fitness = fitness;
            return clone;
        }catch (Exception e){
            System.err.println("\n\ndSolution can't be cloned : CloneNotSupportedException Exception");
            return this;
        }
    }

}
