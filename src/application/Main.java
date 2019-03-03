package application;

import data.representation.Graph;
import data.representation.solutions.Binary_Solution;
import data.representation.solutions.Solution;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import methas.BSO.BSO_Algo;
import methas.GA.G_Algo;
import methas.VNS.VNS_Algo;
import methas.defaultMetha.dAlgo;

import java.util.ArrayList;
import java.util.Collections;


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
        testRead();

    }

    private static void testRead(){
        //Graph graph = new Graph("bench marks\\Range_100\\ins_500_1.txt");
        Graph graph = new Graph("bench marks\\Range_100\\ins_050_1.txt");

        startTime = System.currentTimeMillis() / 1000;


        //dAlgo.Exec(20,10000);
        //VNS_Algo.Exec(5,100000);
        G_Algo.Exec(15,1000);
        //BSO_Algo.Exec(5,2,1000);

//        double min = 100000;
//        int i = 0;
//        Solution Best = new Solution();
//        while (++i<5000){
//            Solution s = new Solution();
//            if(min > s.fitness() ){
//                min = s.fitness();
//                Best = s;
//            }
//        }
//
//        Best.printPerformance();
//
//
//        int i2 = 0;
//        Binary_Solution Best2 = new Binary_Solution();
//        while (++i2<5000){
//            Binary_Solution s = new Binary_Solution();
//            if(min > s.fitness() ){
//                min = s.fitness();
//                Best2 = s;
//            }
//        }
//
//        Best2.printPerformance();




    }

}
