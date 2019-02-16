package data.representation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Node implements Comparable {


    private String name ;
    private HashMap<Node , Double> neighbors = new HashMap<>();
    private ArrayList<Node> neighborsNodes = new ArrayList<>();


    public Node(String name) {
        this.name = name;
        //index = Integer.valueOf(name);
    }


    // @ getters
    public String getName() {
        return name;
    }
    //public int getIndex() { return  index; }

    public HashMap<Node, Double> getNeighbors() {
        return neighbors;
    }
    public ArrayList<Node> getNeighborsNodes(){return neighborsNodes; }

    public Node getRandomNeighbor(){
        int nbNeighbors = neighbors.keySet().size();

        int randomIndx = 0 ;
        if(nbNeighbors > 0)
            randomIndx = ThreadLocalRandom.current()
                    .nextInt(0, nbNeighbors);

        if(randomIndx < neighborsNodes.size())
            return neighborsNodes.get(randomIndx);

        return null;
    }

    public Node getRandomNeighbor(ArrayList<Node> exploredNodes){

        if(exploredNodes.containsAll(neighborsNodes)) return null;
        Node randNode = getRandomNeighbor();
        if(randNode == null) return null;
        if( !exploredNodes.contains(randNode)) return randNode;

        for(Node n:neighborsNodes){
            if(!exploredNodes.contains(n))
                return n;
        }

        // return null if all neighbors are explored
        return null;
    }

    private Node getMinNeighbor(ArrayList<Node> exploredNodes){
        if(exploredNodes.containsAll(neighborsNodes)) return null;

        Node minNeighbor = neighborsNodes.get(0);
        for(Node n:neighborsNodes){
            if(!exploredNodes.contains(n))
                minNeighbor = n;
        }
        if(exploredNodes.contains(minNeighbor))
            return null;

        double minFit = neighbors.get(minNeighbor);

        for(Node n:neighborsNodes){
            if(!exploredNodes.contains(n) &&
                    minFit > neighbors.get(n)){
                minNeighbor = n;
                minFit = neighbors.get(n);
            }
        }

        System.err.println("--- Min neighbor default solution method ---");
        return minNeighbor;
    }


    public double weight(Node neighbor){
        try{
            return neighbors.get(neighbor);
        }catch (Exception e) {
            return 0;
        }
    }

    // @ setters
    public void addNeighbor(Node neighbor , Double weight){
        neighborsNodes.add(neighbor);
        neighbors.put(neighbor,weight);

        neighbor.neighborsNodes.add(this);
        neighbor.getNeighbors().put(this,weight);
    }


    // @ functions
    public Boolean isNeighbor(Node n){
        return neighbors.containsKey(n);
    }




    @Override
    public int compareTo(Object o) {
        if( o == null  || !(o instanceof Node) ) return -1;
        return ((Node) o) .name == name ? 0 : -1;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if( o == null  || !(o instanceof Node) ) return false;
        return ((Node) o) .name == name ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        String out = "";
        out += "\n#######################\n";
        out += "###### Node: @"+name+" { "+neighbors.keySet().size()+" Arcs }\n";
        for(Node n:neighbors.keySet()){
            out+= "###### ---"+neighbors.get(n)+"---->  "+n.name+"\n";
        }
        out += "#######################\n\n";
        return out;
    }

}
