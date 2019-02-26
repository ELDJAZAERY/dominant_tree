package data.representation;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;

public class Arc implements Comparable , Cloneable {

    public Node debut;
    public Node fin;
    private double weight;


    public Arc(Node d , Node f , double weight){
        debut = d;
        fin = f;
        this.weight = weight;
    }

    public Node getNewNode(HashSet<Node> dominoTree){
        if(appartien(dominoTree)) return null;
        if(dominoTree.contains(debut)) return debut;
        if(dominoTree.contains(fin)) return fin;
        return null;
    }

    public boolean appartien(HashSet<Node> dominoTree){
        if(dominoTree.contains(debut) &&
                dominoTree.contains(fin))
            return true;
        return false;
    }

    public boolean isNeighborTo(HashSet<Node> dominTree){
        if(appartien(dominTree)) return false;
        if(dominTree.contains(debut) ||
                dominTree.contains(fin)) return true;
        return false;
    }

    // getters

    public double getWeight() {
        return weight;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        return (int)(weight - ((Arc) o).weight);
    }

    @Override
    public int hashCode() {
        String hashCode;
        if(debut.index > fin.index)
            hashCode = ""+debut.index+""+fin.index;
        else
            hashCode = ""+fin.index+""+debut.index;

        return Objects.hash(hashCode);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if( o == null  || !(o instanceof Arc) ) return false;
        Arc arc = ((Arc) o);

        if((arc.debut.equals(debut) && arc.fin.equals(fin)) ||
                (arc.fin.equals(debut) && arc.debut.equals(fin)))
            return true;
        return false;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "\n" + debut.getName() + " ---- "+weight+" ---- " + fin.getName()+"";
    }
}
