package methas.GA;

import data.representation.Node;
import data.representation.solutions.Binary_Solution;
import data.representation.solutions.Solution;
import methas.VNS.VNSolution;
import methas.defaultMetha.dSolution;

import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

public class GA_Solution extends Solution {

    // 20%
    static int mutationRage = 50;

    static int nbLocalSearch = 100;

    public GA_Solution(){}
    public GA_Solution(Solution n1 , Solution n2){
        int rand = ThreadLocalRandom.current().nextInt(3,n1.size()/2);

        int i = 0;
        for(Node node:n1.dominoTree){
            dominoTree.add(node);
            if(i++ >= rand) break;
        }

        rand = ThreadLocalRandom.current().nextInt((n2.size()/2) + 3,n2.size());
        i = 0;
        for(Node node:n2.dominoTree){
            if(i++ > rand) dominoTree.add(node);
        }

        correction();
    }

//    public static Solution crossOver(){
//
//        HashSet<Node> crossSet = new HashSet<>();
//        Solution CrossSolution = new Solution();
//
//         CrossSolution. n1.dominoTree
//        crossSolution.addAll(n2.dominoTree);
//
//
//        return nStar;
//    }

    public void mutation(){
        int randIndex = ThreadLocalRandom.current()
                .nextInt(0,dominoTree.size());

        dominoTree.remove(get(randIndex));
    }


    public GA_Solution LocalSearch(){

        GA_Solution Best = this , cloneSolution;

        Node oldNode , newNode;
        int cpt = nbLocalSearch;
        while(cpt-- > 0){

            cloneSolution = (GA_Solution) this.clone();

            int k = ThreadLocalRandom.current()
                    .nextInt(1,10);

            for(int i=1 ; i <= k ; i++) {
                if(i % 2 == 0){
                    oldNode = cloneSolution.getRandomNode();
                    newNode = oldNode.getRandomNeighbor(cloneSolution.dominoTree);
                    if(newNode != null){
                        cloneSolution.dominoTree.remove(oldNode);
                        cloneSolution.dominoTree.add(newNode);
                    }{
                        cloneSolution.dominoTree.remove(oldNode);
                    }
                }else{
                    oldNode = cloneSolution.getRandomNode();
                    cloneSolution.dominoTree.remove(oldNode);
                }
            }

            cloneSolution.correction();
            if(Best.compareTo(cloneSolution)>0){
                Best = cloneSolution;
            }
        }

        return Best;
    }


}
