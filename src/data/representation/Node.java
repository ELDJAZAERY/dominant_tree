package data.representation;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Node implements Comparable {

    private int index;
    private String name ;
    private HashMap<Node , Integer> neighbors = new HashMap<>();

    public Node(String name) {
        this.name = name;
        index = Integer.valueOf(name);
    }


    // @ getters
    public String getName() {
        return name;
    }
    public int getIndex() { return  index; }


    public HashMap<Node, Integer> getNeighbors() {
        return neighbors;
    }

    public Node getRandomNeighbor(){
        int nbNeighbors = neighbors.keySet().size();

        int randomNum = 0 ;
        if(nbNeighbors > 0)
            randomNum = ThreadLocalRandom.current()
                    .nextInt(0, nbNeighbors);

        int i=0;
        for(Node n :neighbors.keySet()){
            if(randomNum == i) return n;
            i++;
        }

        return null;
    }


    // @ setters
    public void addNeighbor(Node neighbor , int weight){
        neighbors.put(neighbor,weight);
        neighbor.getNeighbors().put(this,weight);
    }



    @Override
    public int compareTo(Object o) {
        if( o == null  || !(o instanceof Node) ) return -1;
        return ((Node) o) .name == name ? 0 : -1;
    }

    @Override
    public boolean equals(Object o) {
        if( o == null  || !(o instanceof Node) ) return false;
        return ((Node) o) .name == name ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }

}
