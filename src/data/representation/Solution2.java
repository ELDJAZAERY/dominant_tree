package data.representation;

import application.Main;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


public class Solution2 implements Comparable , Cloneable  {


    public static int nbEvaluations = 0;

    protected HashSet<Node> dominoTree ;
    private HashSet<Arc> path = new HashSet<>();
    private double fitness;
    public boolean isSolution;
    private Set<Node> ExploredNode ;


    // @ Generate Default Random dSolution

    public Solution2(){

        // Nodes initial
        Node CurrentNode ;
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

            /* ### explore all Node without find dSolution ### */
            if(CurrentNode == null) {
                System.err.println("### No dSolution -- Graph Non Connexe ###");
                System.exit(-1);
            }

            dominoTree.add(CurrentNode);
            ExploredNode.add(CurrentNode);
            ExploredNode.addAll(CurrentNode.getNeighborsNodes());

            if(Graph.isExplored(ExploredNode)){
                MAJ_sol();
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
        HashSet<Node> afterPruning = new HashSet<>(dominoTree);
        for(Node node : dominoTree){
            if(node.isPurninable(afterPruning))
                afterPruning.remove(node);
            //System.out.println(node.isPurninable(afterPruning));
        }
        dominoTree = afterPruning;
    }


    // Correction Phase

    // TODO NO CORRECTION YET
    public void correction(){

        isSolution = false;
        if(!Graph.DominatesNodes.isEmpty())
            dominoTree.addAll(Graph.DominatesNodes);

        Node CurrentNode;
        HashSet<Node> tempNodes = new HashSet<>();

        CurrentNode =  get(0);
        tempNodes.add(CurrentNode);


        for(Node n:dominoTree){
            if(n.isNeighbor(tempNodes)){
                tempNodes.add(n);
            }
            if(Graph.isDomiTree(tempNodes)) {
                break;
            }
        }


        while(!Graph.isDomiTree(tempNodes)){
            CurrentNode = Graph.getRandomNeighborNode(tempNodes);
            tempNodes.add(CurrentNode);
        }


        dominoTree = tempNodes;
        MAJ_sol();
    }


    public void MAJ_sol(){

        //pruning();
        // connect dominate Node
//        System.out.println("MAJ Solution");
//        System.out.println("befor connect"+dominoTree.size());
        //Connect();
//        System.out.println("after connect befor pruning"+dominoTree.size());

        pruning();
//        System.out.println("after pruning"+dominoTree.size());

        Connect();
//        System.out.println("after connect"+dominoTree.size());

        MST();
//        System.out.println("MAJ Solution\n\n\n");
        //MAJ_Arcs();


        // MAJ Fitness
        MAJ_Fitness();
    }


    public void MST(){

        path.clear();

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
            }else{
                System.out.println("Connect infini boucle "+DominateSet.toString());
            }

        }

        dominoTree = newDominateSet;
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
        return (int)(fitness - ((Solution2) o).fitness);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Solution2)) return false;
        return dominoTree.equals(((Solution2) o).dominoTree);
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
        out += "\tIsDominate  : " + Graph.isDomiTree(dominoTree) + " , \n";
        out += "\tIsConnexe   : " + isConnexe() + "  ,\n";
        out += "\tNb Arcs     : " + path.size() + "  ,\n";
        //out += "\tArcs        : " + path.toString() + "  ,\n";

        out += "}\n";
        
        System.out.println(out);
    }



    @Override
    protected Object clone() {
        Solution2 clone = null;
        try{
            clone = (Solution2) super.clone();
            clone.dominoTree = new HashSet<>(dominoTree);
            clone.fitness = fitness;
            return clone;
        }catch (Exception e){
            System.err.println("\n\ndSolution can't be cloned : CloneNotSupportedException Exception");
            return this;
        }
    }

}
