package application;

import data.representation.Graph;
import data.representation.Solution;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import data.representation.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;


public class Main extends Application {

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

    private static void testRandom(){
        while(true){
            int randomIndex = ThreadLocalRandom.current()
                    .nextInt(0, 5);
            System.out.println(randomIndex);
        }
    }

    private static void test(){
        Node n1 = new Node("1");
        Node n2 = new Node("1");
        Node n3 = new Node("3");
        Node n4 = new Node("4");
        Node n5 = new Node("5");

        Map<Node , Integer> hash = new HashMap<>();
        hash.put(n1,5);
        hash.put(n2,6);
        hash.put(n3,5);
        hash.put(n4,6);
        hash.put(n5,5);

        n1.addNeighbor(n3,3.);
        n1.addNeighbor(n4,34.);
        n1.addNeighbor(n5,345.);

        n4.addNeighbor(n5,34.);

        for(Node  n : hash.keySet()){
            System.out.println(n.getName()+" --- "+hash.get(n));
        }

        System.out.println(n4.getRandomNeighbor());
        System.out.println(n4.getRandomNeighbor());

        System.out.println(n5.getRandomNeighbor());
        System.out.println(n5.getRandomNeighbor());
        /*
        System.out.println(n1.getRandomNeighbor());
        System.out.println(n1.getRandomNeighbor());
        System.out.println(n1.getRandomNeighbor());
        System.out.println(n1.getRandomNeighbor());
        System.out.println(n1.getRandomNeighbor());
        System.out.println(n1.getRandomNeighbor());
        System.out.println(n1.getRandomNeighbor());
        */
    }

    private static void testRead(){
        //Graph graph = new Graph("bench marks\\Range_100\\ins_500_1.txt");
        Graph graph = new Graph("bench marks\\Range_100\\ins_050_2.txt");

        methas.VNS.VNS_Algo.Exec(30,10);

        double min = 100000;
        while (true){
            Solution s = new Solution();
            if(min > s.fitness() )
                min = s.fitness();
            System.out.println(min);
            System.out.println(s.fitness());
            System.out.println("is Connex ? :"+s.isConnexe());
            System.out.println("is dominate ? :"+Graph.isDomiTree(s));
            System.out.println(" --- "+s.size()+" ---\n");
        }


    }

}
