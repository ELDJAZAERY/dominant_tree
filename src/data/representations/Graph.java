package data.representations;

import data.reader.Instances;

import java.util.*;


public class Graph {
    
    public ArrayList<Vertex> vertices;
    public ArrayList<Edge> edges;
    
    
    public Graph() {
        this.vertices = new ArrayList<>();
        this.edges = new ArrayList<>();
    }

    private Graph(Graph clone , ArrayList<Vertex> verticesDT){
        vertices = new ArrayList<>();
        edges = new ArrayList<>();

        for(Vertex v:clone.vertices){
            vertices.add(new Vertex(v.getLabel()));
        }

        Vertex one , two ;
        for(Edge e:clone.edges){
            if(e.isDomin(verticesDT)){
                one = vertices.get(Integer.valueOf(e.getOne().getLabel()));
                two = vertices.get(Integer.valueOf(e.getTwo().getLabel()));

                one.addNeighbor(e);
                two.addNeighbor(e);

                edges.add(new Edge(
                        one,
                        two,
                        e.getWeight()
                ));
            }
        }

        vertices.retainAll(verticesDT);
    }

    public boolean addEdge(Vertex one, Vertex two, float weight) {
        if(one.equals(two)) {
            return false;   
        }
        
        Edge e = new Edge(one, two, weight);

        edges.add(e);
        one.addNeighbor(e);
        one.addNeighbor(two);

        two.addNeighbor(e);
        two.addNeighbor(one);
        return true;
    }

    public void addVertex(Vertex vertex) {
        vertices.add(vertex);
    }

    public void CloneZone(ArrayList<Vertex> verticesDT) {
        vertices.retainAll(verticesDT);

        for(int i = 0 ; i < edges.size() ; i++){
            if(!edges.get(i).isDomin(verticesDT)){
                edges.remove(i);
                i--;
            }
        }

        System.out.println(vertices.size());
        System.out.println(edges.get(0).getOne().toString());
        System.out.println();

        ArrayList<Vertex> vertices = new ArrayList<>();
        HashMap<String,Vertex> verticesMap = new HashMap<>();
        Vertex one , two ;


        for(Vertex v:this.vertices){
            one = new Vertex(v.getLabel());
            vertices.add(one);
            verticesMap.put(v.getLabel(),one);
        }

        for(Edge e:this.edges){
            one = verticesMap.get(e.getOne().getLabel());
            two = verticesMap.get(e.getTwo().getLabel());

            one.addNeighbor(e);
            two.addNeighbor(e);
        }

        this.vertices = vertices;
    }

    public static HashSet<Edge> MST(ArrayList<Vertex> verticesDT) {

        HashSet<Edge> pathDT = new HashSet<>();
        Graph graphDT = new Graph(Instances.graph,verticesDT);


        Vertex nextVertex;
        HashSet<Vertex> vertices = new HashSet<>();
        ArrayList<Edge> ququeArcs = new ArrayList<>();

        nextVertex = graphDT.vertices.get(0);
        vertices.add(nextVertex);

        ququeArcs.addAll(nextVertex.getNeighborsEdges());

        while(!vertices.containsAll(graphDT.vertices)){
            ququeArcs.removeAll(pathDT);
            Collections.sort(ququeArcs);
            for(Edge arc:ququeArcs) {
                if(!arc.contains_vertex(graphDT.vertices)) continue;
                if (!vertices.contains(arc.getTwo())){
                    pathDT.add(arc);
                    vertices.add(arc.getTwo());
                    ququeArcs.addAll(arc.getTwo().getNeighborsEdges());
                    break;
                }else if(!vertices.contains(arc.getOne())){
                    pathDT.add(arc);
                    vertices.add(arc.getOne());
                    ququeArcs.addAll(arc.getOne().getNeighborsEdges());
                    break;
                }
            }
        }

        return pathDT;
    }

}
