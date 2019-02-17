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
            int randomIndx = ThreadLocalRandom.current()
                    .nextInt(0, Nodes.size());
            return Nodes.get(randomIndx);
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
