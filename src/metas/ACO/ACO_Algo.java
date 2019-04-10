package metas.ACO;

import data.reader.Instances;

import java.util.ArrayList;

public class ACO_Algo {

    public static void ACO_Exec(){

        ArrayList<Integer> perm = new ArrayList<>();

        for(int i = 0 ; i < Instances.NbVertices ; i++){
            perm.add(i);
        }

        ACO_Solution sol = new ACO_Solution(perm);

    }

}
