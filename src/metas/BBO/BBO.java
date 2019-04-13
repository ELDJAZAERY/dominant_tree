package metas.BBO;

import data.Logger;
import data.reader.Instances;
import data.representations.Solutions.Solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;


public class BBO {

    private int MaxIterations;

    private int populationSize;
    private ArrayList<Individual> population;

    // Emigration rate (Crossing)
    private ArrayList<Float> EmigrationsPbs;

    // Immigration Rate
    private ArrayList<Float> ImmigrationsPbs;

    // mutation Probabilities
    private float PMutate = (float) 0.005;
    private ArrayList<Float> IndividualPMutate;


    // Diversification
    private int diversificationRate = 30;
    private int diversification = 0 ;

    private Individual Best;


    /** Static */
    public static long startTime   = System.currentTimeMillis();
    public static int  nbIteration = 0;


    private int random(int range){
        range = (range == 0) ? 1 : range;
        return ThreadLocalRandom.current().nextInt(0,range);
    }

	public BBO(int MaxIterations , int populationSize , float PMutate ){

        population = new ArrayList<>();

        IndividualPMutate = new ArrayList<>();
        ImmigrationsPbs = new ArrayList<>();
        EmigrationsPbs = new ArrayList<>();

        this.MaxIterations = MaxIterations;
        this.populationSize = populationSize;
		this.PMutate = PMutate;
    }

    public BBO(int MaxIterations , int populationSize){

        population = new ArrayList<>();

        IndividualPMutate = new ArrayList<>();
        ImmigrationsPbs = new ArrayList<>();
        EmigrationsPbs = new ArrayList<>();

        this.MaxIterations = MaxIterations;
        this.populationSize = populationSize;
    }


	public void BBO_Exec(ArrayList<Solution> populationInitial) {

	    startTime = System.currentTimeMillis();

        InitializePopulation(populationInitial);

        Best = Collections.max(population);
        Best.display();

        nbIteration = 0;

        /**\// ## Updating Population Loop ## \//**/
		for (int i = 0; i < MaxIterations; i++) {
            nbIteration++;

			LinkedList<Individual> elitism = new LinkedList<>();
			elitism.add(population.get(0));
			elitism.add(population.get(1));


			for (int j = 0; j < populationSize; j++) {
				LinkedList<Integer> currentSolutions = new LinkedList<>(population.get(j).sol.permutation);

				// Random 0% --> 100%
				if(random(100) < ImmigrationsPbs.get(j)) {
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
            _MultiThLocalSearch();



            /** Elitism with the worst **/
            Collections.sort(population);
			population.set(populationSize - 1, elitism.get(0));
			population.set(populationSize - 2, elitism.get(1));


            /** evaluate Population **/
			Collections.sort(population);

			if ( Best.sol.fitness - population.get(0).fitness > 0) {
			    Best = population.get(0);
			    Best.display();
                Logger.PersistanceLog("500-1",Best.toString());
			} else {
				diversification++;
			}

			if (diversification > diversificationRate) {
			    /** Diversification **/
			    _Diversity(elitism);
			    diversification = 0;
			}

			UpdatePopulations();
		}

        Best.display();
        System.out.println(" --------- BBO FIN ----------");
	}


	public void InitializePopulation(ArrayList<Solution> populationInitial) {
	    if(populationInitial == null){
            for (int i = 0; i < populationSize; i++) {
                Individual In = new Individual();
                population.add(In);
                ImmigrationsPbs.add(1 - (((float)(populationSize - i)) / populationSize));
                EmigrationsPbs.add( ((float)(populationSize - i)) / populationSize);
            }
            updateProb();
        }else{
            for (int i = 0; i < populationSize; i++) {
                Individual In = new Individual(populationInitial.get(i).permutation);
                population.add(In);

                ImmigrationsPbs.add(1 - (((float)(populationSize - i)) / populationSize));

                EmigrationsPbs.add( ((float)(populationSize - i)) / populationSize);
            }
            updateProb();
        }
	}


	public void UpdatePopulations() {
        for (int i = 0; i < population.size(); i++) {
            ImmigrationsPbs.set(i, 1 - (((float)(populationSize - i)) / populationSize));
            EmigrationsPbs.set(i, ((float)(populationSize - i)) / populationSize);
        }
        updateProb();
	}


    public void updateProb() {

	    IndividualPMutate.clear();

        float sommeLambdaTotal = 0;
        float sommeMuTotal = 0;
        float somme ;

        for (int k = 0; k < populationSize; k++) {
            sommeLambdaTotal = sommeLambdaTotal + ImmigrationsPbs.get(k);
            sommeMuTotal = sommeMuTotal + EmigrationsPbs.get(k);
        }
        somme = sommeLambdaTotal / sommeMuTotal;

        for (int k = 0; k < populationSize; k++) {
            IndividualPMutate.add((1 / (1 + somme)));
        }
    }


    private HashMap<Integer, Integer> permutations;
    private void _Crossing(LinkedList<Integer> currentSolutions , int j){

        permutations = new HashMap<>();
        for (int n = 0; n < currentSolutions.size(); n++) {
            permutations.put(currentSolutions.get(n), n);
        }

        for (int l = 0; l < populationSize && l != j; l++) {

            if (random(100) < EmigrationsPbs.get(l)) {

                int rand = random(population.get(l).sol.verticesDT.size() - 1 );

                int temp = currentSolutions.get(rand);
                int newVertex = population.get(l).sol.permutation.get(rand);
                int pos = permutations.get(newVertex);

                currentSolutions.set(rand, newVertex);
                currentSolutions.set(pos, temp);
                permutations.put(newVertex, rand);
                permutations.put(temp, pos);
            }
        }
    }


    private void _Mutation(LinkedList<Integer> currentSolutions , int j){

        permutations = new HashMap<>();
        for (int n = 0; n < currentSolutions.size(); n++) {
            permutations.put(currentSolutions.get(n), n);
        }

        float mi = PMutate * ( (1 - (IndividualPMutate.get(j))) / Collections.max(IndividualPMutate));
        for (int i = 0; i < Instances.NbVertices ; i++) {

            if (random(100) < mi) {

                int rand;
                while((rand = random(Instances.NbVertices - 1)) ==
                        currentSolutions.get(currentSolutions.get(i)));

                int temp = currentSolutions.get(i);
                int pos   = permutations.get(rand);

                currentSolutions.set(i, rand);
                currentSolutions.set(pos, temp);
                permutations.put(rand, i);
                permutations.put(temp, pos);
            }
        }

    }


    private void _Diversity(LinkedList<Individual> elitism){
        System.out.println(" ---- @Diversification ----");
        diversification = 0;

        // Generate new Population
        for (int k = 0; k < populationSize; k++) {
            Individual I = new Individual();
            population.set(k, I);
        }

        // Elitism
        population.set(populationSize - 1, elitism.get(0));
        population.set(populationSize - 2, elitism.get(1));
        population.set(populationSize - 3, Best);

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
        ArrayList<Integer> permutation;
        Individual current ;
        int temp;

        Individual LocalBest = individual;

        for (int d = 0; d < individual.sol.verticesDT.size(); d++) {
            //for (int v = 0; v < Instances.NbVertices ; v++) {
            for (int v = individual.sol.verticesDT.size(); v < Instances.NbVertices ; v++) {
                permutation = new ArrayList<>(individual.sol.permutation);
                temp = permutation.get(d);
                permutation.set(d, permutation.get(v));
                permutation.set(v, temp);

                current = new Individual(permutation);

                if (current.fitness < LocalBest.fitness) {
                    LocalBest = current;
                }
            }
            individual = LocalBest;
        }

        if (population.get(i).fitness > individual.fitness) {
            population.set(i, individual);
        }
    }

    /** </Local Search> **/

}
