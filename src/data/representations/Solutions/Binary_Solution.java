package data.representations.Solutions;

import data.reader.Instances;
import data.representations.Vertex;
import java.util.*;


public class Binary_Solution extends Solution implements Cloneable {

    public Byte[] binary ;

    public Binary_Solution(Byte[] binary){
        this.binary = binary;
        Binary_TO_Set();
    }

    public Binary_Solution(){
        super();
        Set_To_Binary();
   }

    public Binary_Solution(List<Integer> CurrentSol) {
        super(CurrentSol);
        Set_To_Binary();
    }

    public Binary_Solution(Binary_Solution clone) {
        super(clone);
        Set_To_Binary();
    }

    public void Set_To_Binary(){
        int i = 0;
        binary = new Byte[Instances.NbVertices];
        for(Vertex vertices: Instances.graph.vertices){
                binary[i] = (byte) 1;
                binary[i] = verticesDT.contains(vertices) ? (byte) 1 : (byte) 0;
                i++;
        }
    }

    public void Binary_TO_Set(){
        ArrayList<Integer> permutations = new ArrayList<>();
        for(int i = 0 ; i < Instances.NbVertices;i++){
            if(this.binary[i] == (byte)1)
                permutations.add(i);
        }

        permutation = new ArrayList<>(permutations);
        Correction();
    }

    @Override
    public Object clone(){
        return new Binary_Solution(this);
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
