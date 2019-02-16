package data.representation;

import data.read.BenchToGraph;

import java.util.ArrayList;

public class Graph {

    public String[] toString;
    public ArrayList<Node> Nodes ;

    public Graph(String path) {
        Nodes = data.read.BenchToGraph.convert(path);
        System.out.println(Nodes);
        System.out.println(" -------------- FIN FINAL ----------");
    }


}
