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
    public static ArrayList<Node> DominatesNodes = new ArrayList<>();
    public static ArrayList<Arc>  Arcs = new ArrayList<>();

    public Graph(String path) {
        Nodes        = BenchToGraph.convert(path);
        toString     = BenchToGraph.StringValue;

        int nbArcs ;
        Node tempNode;
        // delete each Node haven't arcs
        for(int i=0;i<Nodes.size();i++){
            tempNode = Nodes.get(i);
            Arcs.addAll(tempNode.getArcs());
            nbArcs = tempNode.getNeighborsNodes().size();
            if( nbArcs == 0 ){
                System.out.println(" --- Node Non Dominate --- "+
                tempNode);
                Nodes.remove(tempNode);
                i--;
            }else if(nbArcs == 1){
                DominatesNodes.add(
                        tempNode.getNeighborsNodes().get(0));
            }
        }
        System.out.println("---- Dominate Nodes Initial --- "+DominatesNodes.toString());
    }

    public static Node getRandomNode(){
        if(Nodes.size() > 0){
            int randomIndex = ThreadLocalRandom.current()
                    .nextInt(0, Nodes.size());
            return Nodes.get(randomIndex);
        }

        return null;
    }


    public static Node getRandomNeighborNode(HashSet<Node> dominateTree){
        if(dominateTree.containsAll(Nodes)) return null;

        Node RandNode = null;

        // N randomly time then brut check
        int i = 25;
        while(RandNode != null && i != 0){
            RandNode = getRandomNode();
            if(RandNode != null){
                if(!dominateTree.contains(RandNode) &&
                        RandNode.isNeighbor(dominateTree))
                    return RandNode;

                RandNode = null;
            }
            i--;
        }

        for(Node n:Nodes){
            if(!dominateTree.contains(n) &&
                    n.isNeighbor(dominateTree))
                return n;
        }

        return null;
    }

    public static Arc getRandomArc(HashSet<Node> domiTree){
        ArrayList<Arc> arcs = arcsNeighbor(domiTree);

        if(arcs.size() == 0) {
            System.out.println("\nNo Neighbor Arcs -- it is a solution ?\n");
            return null;
        }

        int randomIndex = ThreadLocalRandom.current()
                .nextInt(0, arcs.size());

        return arcs.get(randomIndex);
    }


    public static ArrayList<Arc> arcsNeighbor(HashSet<Node> dominaTreee){
        ArrayList<Arc> arcs = new ArrayList<>();
        for(Arc arc:Arcs){
            if(arc.isNeighborTo(dominaTreee))
                arcs.add(arc);
        }
        return arcs;
    }


    public static boolean isDomiTree(Solution s){
        return isDomiTree(s.dominoTree);
    }

    public static boolean isDomiTree(HashSet<Node> domiNodes){
        Set<Node> exploredNodes = new HashSet<>();
        for(Node n:domiNodes){
            exploredNodes.addAll(n.getNeighborsNodes());
        }
        return isExplored(exploredNodes);
    }


    public static boolean isExplored(Set<Node> nodesExplored){
        return nodesExplored.containsAll(Nodes);
    }

}
