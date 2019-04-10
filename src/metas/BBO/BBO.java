package metas.BBO;

import data.Logger;
import data.reader.Instances;
import data.representations.Solutions.Solution;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;


public class BBO {

    private int MaxNbGenerations; // max number of generation

    private int populationSize; // max species count, for each island
    private LinkedList<Individual> population; // the species count probability of each

    private LinkedList<Float> mu; // emigration rate

    private LinkedList<Float> lambda; // immigration rate


    private float PMutate; // initial mutation probability
    private LinkedList<Float> prob; // the species count probability of each habitat

    private Best best;

    private int random(int range){
        range = (range == 0) ? 1 : range;
        return ThreadLocalRandom.current().nextInt(0,range);
    }

    int nbIteration ;
	public BBO(int MaxNbGenerations , int populationSize , float PMutate){

        population = new LinkedList<>();

        prob = new LinkedList<>();
        lambda = new LinkedList<>();
        mu = new LinkedList<>();

        this.MaxNbGenerations = MaxNbGenerations;
        this.populationSize = populationSize;
		this.PMutate = PMutate;
    }

    long startTime = System.currentTimeMillis();


	public void BBO_Exec(ArrayList<Solution> populationInitial) {

        InitializePopulation(populationInitial);

        best = new Best();
        best.update(population.get(0),0,0);
        best.display();

        nbIteration = 0;

        /**\// ## Updating Population Loop ## \//**/
		for (int i = 0; i < MaxNbGenerations; i++) {
            nbIteration++;

			LinkedList<Individual> elitism = new LinkedList<>();
			elitism.add(population.get(0));
			elitism.add(population.get(1));


			for (int j = 0; j < populationSize; j++) {
				LinkedList<Integer> currentSolutions = new LinkedList<>(population.get(j).sol.permutation);

				// Random 0% --> 100%
				if (random(100) < lambda.get(j)) {
				    /** Permutations **/
				    _Crossing(currentSolutions,j);
				}else{
				    /** Mutations **/
                    _Mutation(currentSolutions,j);
                }

				Individual I = new Individual(currentSolutions);
				population.set(j, I);
			}

			/** Local Search Multi Thread **/
            //_MonoThLocalSearch();
            _MultiThLocalSearch();
            //_LocalSearch();


            /** Elitism with the worst **/
            Collections.sort(population);
			population.set(populationSize - 1, elitism.get(0));
			population.set(populationSize - 2, elitism.get(1));


            /** evaluate Population **/
			Collections.sort(population);

			if ( best.cost - population.get(0).cost > 0) {
                best.update(population.get(0),i,startTime);
                best.display();
                Logger.PersistanceLog("500-1",best.toString());
			} else {
				best.div++;
			}

			if (best.div >= 50) {
			    /** Diversification **/
			    _Diversity(elitism);
			}

			UpdatePopulations();
		}

        best.display();
        System.out.println(" --------- BBO FIN ----------");
	}


	public void InitializePopulation(ArrayList<Solution> populationInitial) {
	    if(populationInitial == null){
            for (int i = 0; i < populationSize; i++) {
                Individual In = new Individual();
                population.add(In);
                // lambda(i) is the immigration rate for habitat i
                lambda.add(1 - (((float)(populationSize - i)) / populationSize));
                // mu(i) is the emigration rate for habitat i
                mu.add( ((float)(populationSize - i)) / populationSize);
            }
            updateProb();
        }else{
            for (int i = 0; i < populationSize; i++) {
                Individual In = new Individual(populationInitial.get(i).permutation);
                population.add(In);
                // lambda(i) is the immigration rate for habitat i
                lambda.add(1 - (((float)(populationSize - i)) / populationSize));
                // mu(i) is the emigration rate for habitat i
                mu.add( ((float)(populationSize - i)) / populationSize);
            }
            updateProb();
        }
	}


	public void UpdatePopulations() {
        for (int i = 0; i < population.size(); i++) {
            // lambda(i) is the immigration rate for habitat i
            lambda.set(i, 1 - (((float)(populationSize - i)) / populationSize));
            // mu(i) is the emigration rate for habitat i
            mu.set(i, ((float)(populationSize - i)) / populationSize);
        }
        updateProb();
	}


    public void updateProb() {

	    prob.clear();

        float sommeLambdaTotal = 0;
        float sommeMuTotal = 0;
        float somme ;

        for (int k = 0; k < populationSize; k++) {
            sommeLambdaTotal = sommeLambdaTotal + lambda.get(k);
            sommeMuTotal = sommeMuTotal + mu.get(k);
        }
        somme = sommeLambdaTotal / sommeMuTotal;

        for (int k = 0; k < populationSize; k++) {
            prob.add((1 / (1 + somme)));
        }
    }


    HashMap<Integer, String> permutations;
    private void _Crossing(LinkedList<Integer> currentSolutions , int j){

        permutations = new HashMap<>();
        for (int n = 0; n < currentSolutions.size(); n++) {
            permutations.put(currentSolutions.get(n), String.valueOf(n));
        }

        for (int l = 0; l < populationSize && l != j; l++) {

            if (random(100) < mu.get(l)) {

                int rand = random(population.get(l).sol.verticesDT.size() - 1 );

                int temp = currentSolutions.get(rand);
                int newVertex = population.get(l).sol.permutation.get(rand);
                String pos = permutations.get(newVertex);

                currentSolutions.set(rand, newVertex);
                currentSolutions.set(Integer.parseInt(pos), temp);
                permutations.put(newVertex, String.valueOf(rand));
                permutations.put(temp, pos);
            }
        }
    }


    private void _Mutation(LinkedList<Integer> currentSolutions , int j){

        permutations = new HashMap<>();
        for (int n = 0; n < currentSolutions.size(); n++) {
            permutations.put(currentSolutions.get(n), String.valueOf(n));
        }

        float mi;
        mi = PMutate * ( (1 - (prob.get(j))) / Collections.max(prob));
        for (int i = 0; i < Instances.NbVertices ; i++) {

            if (random(100) < mi) {

                int rand;
                while((rand = random(Instances.NbVertices - 1)) ==
                        currentSolutions.get(currentSolutions.get(i)));

                int temp = currentSolutions.get(i);
                String pos   = permutations.get(rand);
                currentSolutions.set(i, rand);
                currentSolutions.set(Integer.parseInt(pos), temp);
                permutations.put(rand, String.valueOf(i));
                permutations.put(temp, pos);
            }
        }

    }


    private void _Diversity(LinkedList<Individual> elitism){
        System.out.println(" ---- @Diversification ----");

        // Generate new Population
        for (int k = 0; k < populationSize; k++) {
            Individual I = new Individual();
            population.set(k, I);
        }
        best.div = 0;

        // Elitism
        population.set(populationSize - 1, elitism.get(0));
        population.set(populationSize - 2, elitism.get(1));
        population.set(populationSize - 3, best.individual);

        Collections.sort(population);
    }


    /** <Local Search> **/


    private void _MonoThLocalSearch(){
        for (int I = 0; I < populationSize; I++) {
            _Individual_Search(I);
        }
    }


    /** Local Search Multi Thread **/
    private void _MultiThLocalSearch(){

        ArrayList<Callable<Void>> taskList = new ArrayList<>();
        //for (int j = 0; j < Math.min(populationSize,nbIteration) ; j++) {
        //for (int j = 0; j < 5 ; j++) {
        for (int j = 0; j < populationSize; j++) {
            final int th = j;
            Callable<Void> callable = () -> {
                _Individual_Search(th);
                return null;
            };
            taskList.add(callable);
        }

        ExecutorService executor = Executors.newFixedThreadPool(populationSize);
        try {
            executor.invokeAll(taskList);
        } catch (InterruptedException ie) {
        }
    }

    /**  Local search individual **/
    private void _Individual_Search(int i) {

        Individual individual = population.get(i);
        LinkedList<Integer> permutation;
        Individual current ;
        int temp;

        Individual LocalBest = individual;

        for (int t = 0; t < individual.sol.verticesDT.size(); t++) {
                for (int v = 0; v < Instances.NbVertices ; v++) {
                    permutation = new LinkedList<>(individual.sol.permutation);
                    temp = permutation.get(t);
                    permutation.set(t, permutation.get(v));
                    permutation.set(v, temp);

                    current = new Individual(permutation);

                    if (current.cost < LocalBest.cost) {
                        LocalBest = current;
                    }
                }
                individual = LocalBest;
        }

        if (population.get(i).cost > individual.cost) {
            population.set(i, individual);
        }
    }

    /** </Local Search> **/


    /** Local Search Multi Thread **/
    private void _LocalSearch(){
        /** <Local Search> **/
        ArrayList<Callable<Void>> taskList = new ArrayList<>();
        for (int j = 0; j < populationSize; j++) {
            final int th = j;
            Callable<Void> callable = () -> {
                _VNS_Function_I(th);
                return null;
            };
            taskList.add(callable);
        }

        ExecutorService executor = Executors.newFixedThreadPool(populationSize);
        try {
            executor.invokeAll(taskList);
        } catch (InterruptedException ie) {
        }
        /** </Local Search> **/

    }

    /**  Local search individual **/
    private void _VNS_Function_I(int i) {

        Individual I_p = population.get(i);
        boolean stop = false;

        while (!stop) {
            stop = true;
            for (int t = 0; t < I_p.sol.verticesDT.size(); t++) {
                boolean find = false;
                Individual I_pc = I_p;
                for (int v = I_p.sol.verticesDT.size(); v < Instances.NbVertices && !find; v++) {
                    LinkedList<Integer> permutation_pp = new LinkedList<>(I_p.sol.permutation);
                    int temp = permutation_pp.get(t);
                    permutation_pp.set(t, permutation_pp.get(v));
                    permutation_pp.set(v, temp);

                    Individual I_pp = new Individual(permutation_pp);

                    if (I_pp.cost < I_pc.cost) {
                        I_pc = I_pp;
                    }
                }
                I_p = I_pc;
            }
            // End of Local Search
            if (population.get(i).cost > I_p.cost) {
                population.set(i, I_p);
            }
        }
    }




}
