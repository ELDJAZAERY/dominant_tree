package application;

import data.representation.Graph;
import data.representation.Solution;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import methas.VNS.VNS_Algo;
import methas.defaultMetha.dAlgo;


public class Main extends Application {

    public static double startTime = 0;


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("application.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    public static void main(String[] args) {
        //launch(args);
        //test();
        testRead();
        //testRandom();
    }

    private static void testRead(){
        //Graph graph = new Graph("bench marks\\Range_100\\ins_500_1.txt");
        Graph graph = new Graph("bench marks\\Range_100\\ins_500_2.txt");

        startTime = System.currentTimeMillis() / 1000;


        dAlgo.Exec(5,1000);
        //VNS_Algo.Exec(30,1000);

/*
        double min = 100000;
        int i = 0;
        Solution Best = new Solution();
        while (++i<1000){
            Solution s = new Solution();
            if(min > s.fitness() ){
                min = s.fitness();
                Best = s;
            }
        }

        Best.printPerformance();
        System.out.println("is Connex ? :"+Best.isConnexe());
        System.out.println("is dominate ? :"+Graph.isDomiTree(Best));
*/

    }

}
