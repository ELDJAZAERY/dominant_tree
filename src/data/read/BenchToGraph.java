package data.read;

import data.representation.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class BenchToGraph {

    public static int nbNodes;
    public static int nbArcs;


    private static ArrayList<Node> tempNodes = new ArrayList<>();


    public static ArrayList<Node> convert(String path){

        String[] lines = readFile(path);

        String[] info = lines[0].split(" ");
        nbNodes = Integer.valueOf(info[0]);
        nbArcs  = Integer.valueOf(info[1]);

        initNodes();
        initArcs();

        return tempNodes;
    }


    private static void initNodes(){
        for(int i=0 ; i < nbNodes ; i++ ){
            tempNodes.add(new Node(""+i));
        }
    }

    private static void initArcs(){

    }

    private static String[] readFile(String path) {
        String content = null;
        File file = new File(path); // For example, foo.txt
        FileReader reader = null;
        try {
            reader = new FileReader(file);
            char[] chars = new char[(int) file.length()];
            reader.read(chars);
            content = new String(chars);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(reader != null){
                try{reader.close();}catch(Exception e){}
            }
        }

        return content.split("\n");
    }

}
