package data.representations.Solutions;

import data.Logger;
import data.reader.Instances;
import data.representations.Edge;
import data.representations.Graph;
import data.representations.Vertex;

import java.util.*;


public class Solution implements Cloneable , Comparable<Solution> {


    /** Solution props **/
    public ArrayList<Integer> permutation;
    public ArrayList<Vertex> verticesDT;
    public HashSet<Edge> path;
    public float fitness;


    /** #Static Permutation initial of the instance actual **/
    private static ArrayList<Integer> permutationInitial = new ArrayList<>();


    /** #Static #Nb Total of solutions **/
    public static int NbEvalsTotal = 0;
    private int nbEvaluations ;


    /** #Static Timer **/
    public static long StartTime = System.currentTimeMillis();
    protected double sec = 0;
    protected int   nbIteration = 0;



    public Solution() {
        nbEvaluations = NbEvalsTotal++;
        if(permutationInitial.size() != Instances.NbVertices){
            permutationInitial.clear();
            for (int j = 0; j < Instances.NbVertices; j++) {
                permutationInitial.add(j);
            }
        }

        Collections.shuffle(permutationInitial);
        permutation = new ArrayList<>(permutationInitial);
        DT();
    }


    public Solution(List<Integer> CurrentSol) {
        nbEvaluations = NbEvalsTotal++;
        if(permutationInitial.size() != Instances.NbVertices){
            permutationInitial.clear();
            for (int j = 0; j < Instances.NbVertices; j++) {
                permutationInitial.add(j);
            }
        }
        permutation = new ArrayList<>(CurrentSol);
        DT();
    }

    protected Solution(Solution clone) {
        permutation = clone.permutation;
        verticesDT = clone.verticesDT;
        path = clone.path;
        fitness = clone.fitness;

        nbEvaluations = NbEvalsTotal;
    }


    /** Getters **/
    public ArrayList<Integer> getPermutation() {
        return permutation;
    }

    public ArrayList<Vertex> getVerticesDT() {
        return verticesDT;
    }




    public void Correction(){
        DT();
    }
    private void DT(){
        verticesDT = DominatingSet();
        Connect();
        pruning();
        MST();
        //MSTGraph();
    }


    public ArrayList<Vertex> DominatingSet() {
        /** Correction phase **/
        if(permutation.size() < Instances.graph.vertices.size()){
            //System.out.println(" --- #Correction phase ---");
            Collections.shuffle(permutationInitial);
            for(Integer vertex:permutationInitial)
                if(!permutation.contains(vertex)){
                    permutation.add(vertex);
                }
        }

        ArrayList<Vertex> dominating_set = new ArrayList<>();

        int[] temp = new int[permutation.size()];
        int computer = 0, j = 0;

        while (j < permutation.size() && computer != permutation.size()) {
            if (temp[permutation.get(j)] == 0) {
                temp[permutation.get(j)] = 1;
                computer++;
            }

            dominating_set.add(Instances.graph.vertices.get(permutation.get(j)));
            Vertex V = Instances.graph.vertices.get(permutation.get(j));
            for (int k = 0; k < V.getNeighborCount(); k++) {
                int v = Integer.parseInt(V.getNeighbor(k).getTheOtherOne(V).getLabel());

                if (temp[v] == 0) {
                    temp[v] = 1;
                    computer++;
                }
            }
            j++;

        }

        return dominating_set;
    }


    public void MSTGraph(){
        path = Graph.MST(verticesDT);
        MAJ_Fitness();
    }


    public void MST(){

        path = new HashSet<>();

        Vertex nextVertex;
        HashSet<Vertex> vertices = new HashSet<>();
        ArrayList<Edge> ququeArcs = new ArrayList<>();

        nextVertex = verticesDT.get(0);
        vertices.add(nextVertex);
        ququeArcs.addAll(nextVertex.getDominNeighborsEdges(verticesDT));

        while(!vertices.containsAll(verticesDT)){
            ququeArcs.removeAll(path);
            Collections.sort(ququeArcs);
            for(Edge arc:ququeArcs) {
                //if(!arc.contains_vertex(verticesDT)) continue;
                if (!vertices.contains(arc.getTwo())){
                    path.add(arc);
                    vertices.add(arc.getTwo());
                    ququeArcs.addAll(arc.getTwo().getDominNeighborsEdges(verticesDT));
                    break;
                }else if(!vertices.contains(arc.getOne())){
                    path.add(arc);
                    vertices.add(arc.getOne());
                    ququeArcs.addAll(arc.getOne().getDominNeighborsEdges(verticesDT));
                    break;
                }
            }
        }

        MAJ_Fitness();
    }


    public void MAJ_Fitness(){
        fitness = 0;
        if(verticesDT.size() == 0 || path.size() == 0) return;
        for(Edge edge:path){
            fitness += edge.getWeight();
        }
        nbEvaluations++;
    }

    
    private void pruning(){
        HashSet<Vertex> afterPruning = new HashSet<>(verticesDT);
        for(Vertex v : verticesDT){
            if(v.isPurninable(afterPruning))
                afterPruning.remove(v);
        }
        verticesDT = new ArrayList<>(afterPruning);
    }



    /**
     *
     *
     * Simple Connect and Alternative
     * @Connect Method
     *
     *
     *
     * **/

    private void connect(){
        while(!isConnected()){
            verticesDT.add(Instances.graph.vertices.get(permutation.get(verticesDT.size())));
        }
    }

    // TODO Optimization of this method
    private boolean isConnected(){

        if(verticesDT.isEmpty()) return false;

        ArrayList<Vertex> vertices = new ArrayList<>(verticesDT);
        HashSet<Vertex> tempTree = new HashSet<>();

        tempTree.add(vertices.get(0));
        vertices.remove(0);

        Vertex tempV;
        for(int i=0;i<vertices.size();i++) {
            tempV = vertices.get(i);

            if (tempV.isNeighbor(tempTree)){
                tempTree.add(tempV);
                vertices.remove(i);
                i = -1;
            }
        }

        return vertices.isEmpty();
    }


    public void Connect(){

        ArrayList<Vertex> DominateSet = new ArrayList<>(verticesDT);
        HashSet<Vertex> newDominateSet = new HashSet<>();

        HashSet<Vertex> path0 , path1 , path2 ;


        Vertex maxVertex = getHaveMaxDominNeighbor(DominateSet);
        path0 = maxVertex.getDominNeighbors(DominateSet);

        newDominateSet.add(maxVertex);
        newDominateSet.addAll(path0);

        DominateSet.remove(maxVertex);
        DominateSet.removeAll(path0);


        while(!DominateSet.isEmpty()){

            maxVertex = DominateSet.get(0);

            path0 = getPath0(newDominateSet,DominateSet,maxVertex,false);

            if(!path0.isEmpty()){
                newDominateSet.addAll(path0);
                DominateSet.removeAll(path0);
                continue;
            }


            path1 = getPath1(newDominateSet,DominateSet,maxVertex,false);

            if(!path1.isEmpty()){
                newDominateSet.addAll(path1);
                DominateSet.removeAll(path1);
                continue;
            }



            path2 = getPath2(newDominateSet,DominateSet,maxVertex,false);

            if(!path2.isEmpty()){
                newDominateSet.addAll(path2);
                DominateSet.removeAll(path2);
                continue;
            }

        }

        verticesDT = new ArrayList<>(newDominateSet);
    }


    public HashSet<Vertex> getPath0(HashSet<Vertex> newDominateSet , ArrayList<Vertex> DominateSet , Vertex MaxVertex , boolean onlyForMAxVertex){

        HashSet<Vertex> path0 = new HashSet<>();

        HashSet<Vertex>  MaxVertexDN  , tempVertexDN ;

        if(MaxVertex != null && MaxVertex.isNeighbor(newDominateSet)){
            MaxVertexDN = MaxVertex.getDominNeighbors(DominateSet);

            path0.add(MaxVertex);
            path0.addAll(MaxVertexDN);

            return path0;
        }

        if(onlyForMAxVertex) return path0;

        for(Vertex vertex:DominateSet){
            if(vertex.isNeighbor(newDominateSet)){
                tempVertexDN = vertex.getDominNeighbors(DominateSet);

                path0.add(vertex);
                path0.addAll(tempVertexDN);

                return path0;
            }
        }

        return path0;
    }


    public HashSet<Vertex> getPath1(HashSet<Vertex> newDominateSet , ArrayList<Vertex> DominateSet , Vertex MaxVertex ,boolean onlyForMAxVertex){

        HashSet<Vertex> path1 = new HashSet<>();

        if(MaxVertex != null){
            for(Vertex vertex :MaxVertex.getNeighbors()){
                path1 = getPath0(newDominateSet,DominateSet,vertex,true);
                if(!path1.isEmpty()) {
                    path1.add(MaxVertex);
                    path1.addAll(MaxVertex.getDominNeighbors(DominateSet));
                    return path1;
                }
            }
        }

        if(onlyForMAxVertex) return path1;


        for(Vertex vertex:DominateSet){
            for(Vertex vertex2 : vertex.getNeighbors()){
                path1 = getPath0(newDominateSet,DominateSet,vertex2,true);
                if(!path1.isEmpty()) {
                    path1.add(vertex);
                    path1.addAll(vertex.getDominNeighbors(DominateSet));
                    return path1;
                }
            }
        }

        return path1;
    }


    public HashSet<Vertex> getPath2(HashSet<Vertex> newDominateSet , ArrayList<Vertex> DominateSet ,Vertex MaxVertex,boolean onlyForMAxVertex){

        HashSet<Vertex> path2 = new HashSet<>();

        if(MaxVertex != null){
            for(Vertex vertex :MaxVertex.getNeighbors()){
                path2 = getPath0(newDominateSet,DominateSet,vertex,true);
                if(!path2.isEmpty()) {
                    path2.add(MaxVertex);
                    path2.addAll(MaxVertex.getDominNeighbors(DominateSet));
                    return path2;
                }
            }
        }

        if(onlyForMAxVertex) return path2;


        for(Vertex vertex:DominateSet){
            for(Vertex vertex2 : vertex.getNeighbors()){
                path2 = getPath1(newDominateSet,DominateSet,vertex2,true);
                if(!path2.isEmpty()) {
                    path2.add(vertex);
                    path2.addAll(vertex.getDominNeighbors(DominateSet));
                    return path2;
                }
            }
        }

        return path2;
    }


    public Vertex getHaveMaxDominNeighbor(List<Vertex> dominSet){

        int max = 0;
        Vertex maxVertex = dominSet.get(0);

        for(Vertex vertex:dominSet){
            int nbDN = vertex.getDominNeighborsEdges(dominSet).size();
            if( nbDN > max){
                maxVertex = vertex;
                max = nbDN;
            }
        }

        return maxVertex;
    }

    
    public void display(int nbIteration){
        this.nbIteration = nbIteration;
        display();
    }

    public void display(){
        if(sec == 0 ) {
            long endTime_best = System.currentTimeMillis();
            sec = (endTime_best - StartTime);
            sec = (sec / 1000.1);
        }
        System.out.println(toString());

        /** Logs file **/
        Logger.PersistanceLog(toString());
    }


    @Override
    public String toString() {
        String out = "";
        out += "Best {";
        out += "\n\t Fitness    : " + fitness;
        out += "\n\t Vertices   : " + verticesDT.size();
        out += "\n\t Iteration  : " + nbIteration;
        out += "\n\t nb Sols    : " + nbEvaluations;
        out += "\n\t Secs       : " + sec;
        out += "\n}";
        return out;
    }

    @Override
    public Object clone(){
        return new Solution(this);
    }


    @Override
    public int compareTo(Solution other) {
        float diff = this.fitness - other.fitness;
        return diff < 0 ? -1 : diff > 0 ? 1 : 0;
    }

}
