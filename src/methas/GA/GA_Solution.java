package methas.GA;

import data.representation.solutions.Binary_Solution;
import methas.VNS.VNSolution;

import java.util.concurrent.ThreadLocalRandom;

public class GA_Solution extends Binary_Solution{

    // 20%
    static int mutationRage = 50;

    static int nbLocalSearch = 10;

    public static GA_Solution crossOver(GA_Solution n1 , GA_Solution n2){
        Byte[] bin1 , bin2 , binStar;

        bin1 = n1.binary ; bin2 = n2.binary;
        binStar = new Byte[bin1.length];

        int mutationBorne = ( mutationRage * bin1.length ) / 100;

//        for(int i = 0 ; i < mutationBorne ; i++){
//            binStar[i] = bin1[i];
//        }

        for(int i = 0 ; i < bin1.length ; i++){
            if(ThreadLocalRandom.current().nextInt(0,100)>50){
                binStar[i] = bin1[i];
            }else {
                binStar[i] = bin2[i];
            }
        }

        GA_Solution nStar = new GA_Solution();
        nStar.binary = binStar ;
        nStar.correctionBinary();

        return nStar;
    }

    public void mutation(){
        int randIndex = ThreadLocalRandom.current()
                .nextInt(0,binary.length);

        binary[randIndex] = (binary[randIndex] == 0)? (byte)1 : (byte)0;
        correctionBinary();
    }


    public GA_Solution LocalSearch(){
        GA_Solution sol ;

        for(int i = 1 ; i <= nbLocalSearch ; i++){
            sol = (GA_Solution) this.clone();

            int randIndex = ThreadLocalRandom.current()
                    .nextInt(0,sol.binary.length);

            sol.binary[randIndex] = (sol.binary[randIndex] == 0)? (byte)1 : (byte)0;
            sol.correctionBinary();
            if(compareTo(sol)>0) return sol;
        }
        return this;
    }


}
