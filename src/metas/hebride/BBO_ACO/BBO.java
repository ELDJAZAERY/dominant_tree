package metas.hebride.BBO_ACO;

import data.reader.Instances;
import data.representations.Solutions.Solution;
import metas.ACO.ACO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;


public class BBO {

    private static int MaxIterations;

    private static int populationSize;
    private static ArrayList<Solution> population;

    // Emigration rate (Crossing)
    private static ArrayList<Float> EmigrationsPbs;

    // Immigration Rate
    private static ArrayList<Float> ImmigrationsPbs;

    // mutation Probabilities
    private static float PMutate ;
    private static ArrayList<Float> IndividualPMutate;


    // Diversification
    private static int diversificationRate = 15;
    private static int diversification ;

    private static Solution Best;


    public static int  nbIteration = 0;

    /** Timer Comperation  **/
    public static void BBOTime(){
        long startTime = System.currentTimeMillis();

        Solution best = new Solution(), current;
        for(int i = 0 ; i< 100 ; i++){
            current = new Solution();
            System.out.println(current.fitness);
            current = new Solution(current.permutation);
            System.out.println(current.fitness);
            if(current.compareTo(best)<0){
                best = current;
            }
        }

        System.out.println(best.fitness);
        long endTime_best = System.currentTimeMillis();
        long t = (endTime_best - startTime) / 1000;
        System.out.println(t);
    }


    private static int random(int range){
        range = (range == 0) ? 1 : range;
        return ThreadLocalRandom.current().nextInt(0,range);
    }


    private static void setParams(int MaxIterations , int populationSize , float PMutate ){
        population = new ArrayList<>();

        IndividualPMutate = new ArrayList<>();
        ImmigrationsPbs = new ArrayList<>();
        EmigrationsPbs = new ArrayList<>();

        BBO.MaxIterations = MaxIterations;
        BBO.populationSize = populationSize;
        BBO.PMutate = PMutate;
    }


    public static void BBO_Exec(){
        setParams(50,10,(float)0.05);
        InitializePopulation(null);
        Exec();
    }

    public static void BBO_Exec(int MaxIterations , int populationSize , double PMutate ){
        setParams(MaxIterations,populationSize,(float)PMutate);
        InitializePopulation(null);
        Exec();
    }

    public static void BBO_Exec(int MaxIterations , int populationSize , double PMutate , ArrayList<Solution> population){
        setParams(MaxIterations,populationSize,(float)PMutate);
        InitializePopulation(population);
        Exec();
    }


    public static void BBO_Exec(ArrayList<Solution> population){
        setParams(50,10,(float)0.05);
        InitializePopulation(population);
        Exec();
    }

    public static ArrayList<Solution> getPopulation() {
        return population;
    }

    private static void Exec() {

        Best = Collections.max(population);
        Best.display();

        nbIteration = 0;

        /**\// ## Updating Population Loop ## \//**/
		for (int i = 0; i < MaxIterations; i++) {
            nbIteration++;

            System.out.println(" --- Iteration 1 ---");

			ArrayList<Solution> elitism = new ArrayList<>();
			elitism.add(population.get(0));
			elitism.add(population.get(1));
            elitism.add(population.get(2));


			for (int j = 0; j < populationSize; j++) {
                ArrayList<Integer> currentSolutions = new ArrayList<>(population.get(j).permutation);

				// Random 0% --> 100%
				if(random(100) < ImmigrationsPbs.get(j)) {
				    /** Permutations **/
				    _Crossing(currentSolutions,j);
				}else{
				    /** Mutations **/
                    _Mutation(currentSolutions,j);
                }

				Solution I = new Solution(currentSolutions);
				population.set(j, I);
			}

			/** Local Search Multi Thread **/
            //_MultiThLocalSearch();
            _MonoThLocalSearch();


            /** Elitism with the worst **/
            Collections.sort(population);
			population.set(populationSize - 1, elitism.get(0));
			population.set(populationSize - 2, elitism.get(1));


            /** evaluate Population **/
			Collections.sort(population);

			if ( Best.fitness - population.get(0).fitness > 0) {
			    Best = population.get(0);
			    Best.display(nbIteration);
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


    private static void InitializePopulation(ArrayList<Solution> populationInitial) {
	    if(populationInitial == null){
            for (int i = 0; i < populationSize; i++) {
                Solution In = new Solution();
                population.add(In);
                ImmigrationsPbs.add(1 - (((float)(populationSize - i)) / populationSize));
                EmigrationsPbs.add( ((float)(populationSize - i)) / populationSize);
            }
            updateProb();
        }else{
            for (int i = 0; i < populationSize; i++) {
                Solution In = new Solution(populationInitial.get(i).permutation);
                population.add(In);

                ImmigrationsPbs.add(1 - (((float)(populationSize - i)) / populationSize));

                EmigrationsPbs.add( ((float)(populationSize - i)) / populationSize);
            }
            updateProb();
        }
	}


    private static void UpdatePopulations() {
        for (int i = 0; i < population.size(); i++) {
            ImmigrationsPbs.set(i, 1 - (((float)(populationSize - i)) / populationSize));
            EmigrationsPbs.set(i, ((float)(populationSize - i)) / populationSize);
        }
        updateProb();
	}


    private static void updateProb() {

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


    private static HashMap<Integer, Integer> permutations;
    private static void _Crossing(ArrayList<Integer> currentSolutions , int j){

        permutations = new HashMap<>();
        for (int n = 0; n < currentSolutions.size(); n++) {
            permutations.put(currentSolutions.get(n), n);
        }

        for (int l = 0; l < populationSize && l != j; l++) {

            if (random(100) < EmigrationsPbs.get(l)) {

                int rand = random(population.get(l).verticesDT.size() - 1 );

                int temp = currentSolutions.get(rand);
                int newVertex = population.get(l).permutation.get(rand);
                int pos = permutations.get(newVertex);

                currentSolutions.set(rand, newVertex);
                currentSolutions.set(pos, temp);
                permutations.put(newVertex, rand);
                permutations.put(temp, pos);
            }
        }
    }


    private static void _Mutation(ArrayList<Integer> currentSolutions , int j){

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


    private static void _Diversity(ArrayList<Solution> elitism){
        System.out.println(" ---- @Diversification ----");
        diversification = 0;

        // Generate new Population
        for (int k = 0; k < populationSize; k++) {
            Solution I = new Solution();
            population.set(k, I);
        }

        // Elitism 4 Individuals
        population.set(populationSize - 1, elitism.get(0));
        population.set(populationSize - 2, elitism.get(1));
        population.set(populationSize - 3, elitism.get(2));
        population.set(populationSize - 4, Best);

        Collections.sort(population);
    }



    /** <Local Search> **/

    private static void _MonoThLocalSearch(){
        for (int I = 0; I < populationSize; I++) {
            _Individual_Search(I);
        }
    }


    /** Local Search Multi Thread **/
    private static void _MultiThLocalSearch(){

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
    private static void _Individual_Search(int i) {

        Solution individual = population.get(i);

        individual = ACO.ACO_As_LocalSearch(individual);

        population.set(i, individual);
    }

    /** </Local Search> **/

}
