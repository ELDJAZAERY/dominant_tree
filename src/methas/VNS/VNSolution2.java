package methas.VNS;

import data.representation.solutions.Binary_Solution;

import java.util.concurrent.ThreadLocalRandom;

public class VNSolution2 extends Binary_Solution implements Comparable ,Cloneable {

    static int nbLocalSearch = 500;

    public VNSolution2(){}


    public VNSolution2 shaking(int K) {
        int randIndex1 , randIndex2;
        int tempBinVal;

        VNSolution2 newSol = (VNSolution2) this.clone();
        Byte[] binary = newSol.binary;

        for(int i= 0;i<=K;i++){

            randIndex1 = ThreadLocalRandom.current()
                        .nextInt(0,binary.length);

            randIndex2 = ThreadLocalRandom.current()
                    .nextInt(0,binary.length);

            tempBinVal = binary[randIndex1];
            binary[randIndex1] = binary[randIndex2];
            binary[randIndex2] = (byte)tempBinVal;
        }

        newSol.correctionBinary();
        return newSol;
    }


    public VNSolution2 LocalSearch(){
        VNSolution2 sol ;

        for(int i = 1 ; i <= nbLocalSearch ; i++){
            sol = (VNSolution2)this.clone();
            Byte[] binary = sol.binary;

            int randIndex = ThreadLocalRandom.current()
                    .nextInt(0,binary.length);

            binary[randIndex] = (binary[randIndex] == 0)? (byte)1 : (byte)0;
            sol.correctionBinary();
            if(compareTo(sol)>0) return sol;
        }
        return this;
    }


    @Override
    protected Object clone() {
        return super.clone();
    }

    @Override
    public int compareTo(Object o) {
        return super.compareTo(o);
    }

}
