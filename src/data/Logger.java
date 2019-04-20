package data;

import java.io.PrintWriter;

public class Logger {

    private static String Logs = "";

    public static String id = "1";
    public static String Range = "100";
    public static String LoggerFileName = "Logs/Logs.txt";

    public static void Log(String fileName , String log){
        try {
            PrintWriter writer = new PrintWriter(fileName, "UTF-8");
            writer.println(log);
            writer.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void PersistanceLog(String log){
        try {
            PrintWriter writer = new PrintWriter(LoggerFileName , "UTF-8");
            Logs += log+"\n";
            writer.println(Logs);
            writer.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
