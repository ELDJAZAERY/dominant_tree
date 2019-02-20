package data.representation;

import data.read.BenchToGraph;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Graph {

    public static String[] toString;
    public static ArrayList<Node> Nodes ;


    public Graph(String path) {
        Nodes    = BenchToGraph.convert(path);
        toString = BenchToGraph.StringValue;

        // delete each Node haven't arcs
        for(int i=0;i<Nodes.size();i++){
            if(Nodes.get(i).getNeighborsNodes().size() == 0){
                Nodes.remove(i);
                i--;
            }
        }
    }


    public static Node getRandomNode(){
        if(Nodes.size() > 0){
            int randomIndex = ThreadLocalRandom.current()
                    .nextInt(0, Nodes.size());
            return Nodes.get(randomIndex);
        }

        return null;
    }

    public static Node getRandomNode(Set<Node> exploredNodes){
        if(exploredNodes.containsAll(Nodes)) return null;
        Node randNode = getRandomNode();
        if(randNode == null) return null;
        if( !exploredNodes.contains(randNode)) return randNode;

        for(Node n:Nodes){
            if(!exploredNodes.contains(n))
                return n;
        }

        return null;
    }



    public static Node getRandomNode(ArrayList<Node> exploredNodes){
        if(exploredNodes.containsAll(Nodes)) return null;
        Node randNode = getRandomNode();
        if(randNode == null) return null;
        if( !exploredNodes.contains(randNode)) return randNode;

        for(Node n:Nodes){
            if(!exploredNodes.contains(n))
                return n;
        }
        Node n = null;
        return n;
    }


    public static Node getRandomNode(LinkedList<Node> exploredNodes){
        return getRandomNode(new LinkedList<>(exploredNodes));
    }

    public static Node getRandomNeighborNode(ArrayList<Node> dominateTree){
        if(dominateTree.containsAll(Nodes)) return null;

        Node RandNode = null;
        int i = 5;
        while(RandNode != null && i != 0 ){
            RandNode = getRandomNode();
            if(RandNode != null){
                if(!dominateTree.contains(RandNode) &&
                        RandNode.isNeighbor(dominateTree))
                    return RandNode;

                RandNode = null;
            }
        }

        for(Node n:Nodes){
            if(!dominateTree.contains(n) &&
                    n.isNeighbor(dominateTree))
                return n;
        }

        return null;
    }


    public static boolean isExplored(Set<Node> nodesExplored){
        return nodesExplored.containsAll(Nodes);
    }


    public static boolean isDomiTree(ArrayList<Node> nodes){

        Set<Node> exploredNodes = new HashSet<>();
        for(Node n:nodes){
            exploredNodes.addAll(n.getNeighborsNodes());
        }
        return isExplored(exploredNodes);
    }

    public static boolean isDomiTree(LinkedList<Node> nodes){
        return  isDomiTree(new ArrayList<>(nodes));
    }

}
