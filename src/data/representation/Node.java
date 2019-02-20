package data.representation;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;
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
        int nbNeighbors = neighborsNodes.size();

        int randomIndex = 0 ;
        if(nbNeighbors > 0)
            randomIndex = ThreadLocalRandom.current()
                    .nextInt(0, nbNeighbors);

        if(randomIndex < neighborsNodes.size())
            return neighborsNodes.get(randomIndex);

        return null;
    }

    public Node getRandomNeighbor(LinkedList<Node> exploredNodes){

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

    public Node getRandomNeighbor(ArrayList<Node> exploredNodes){
        LinkedList<Node> linkedlist = new LinkedList<>(exploredNodes);
        return getRandomNeighbor(linkedlist);
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
    public boolean isNeighbor(Node n){
        return neighbors.containsKey(n);
    }

    public boolean isNeighbor(ArrayList<Node> nodes){
        for(Node n:nodes)
            if(isNeighbor(n)) return true;
        return false;
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
