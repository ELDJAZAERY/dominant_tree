package data.representation;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Node implements Comparable {

    private int index;
    private String name ;
    private HashMap<Node , Double> neighbors = new HashMap<>();

    public Node(String name) {
        this.name = name;
        index = Integer.valueOf(name);
    }


    // @ getters
    public String getName() {
        return name;
    }
    public int getIndex() { return  index; }

    public HashMap<Node, Double> getNeighbors() {
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
    public void addNeighbor(Node neighbor , Double weight){
        neighbors.put(neighbor,weight);
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
