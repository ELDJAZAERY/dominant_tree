package data;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Paths;

public class Logger {

    public static String Logs = "";

    public static String LogsRepos;
    public static String LoggerFileName = "Logs.txt";
    public static String LoggerFile;
    static {
        String localRep = Paths.get(".").toAbsolutePath().normalize().toString();
        new File(localRep+"\\NEWLOGS\\").mkdirs();
        LogsRepos = new File(localRep+"\\LOGS\\").getPath();
    }

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
            PrintWriter writer = new PrintWriter(LoggerFile , "UTF-8");
            Logs += log+"\n";
            writer.println(Logs);
            writer.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void initPersistanceLogs(){
        Logs = "";
    }

    public static void initPersistanceLogs(String instance){

        Logs = "";
        LoggerFileName = instance ;
        new File(LogsRepos).mkdirs();
        LoggerFile = LogsRepos+"\\LOGS-"+LoggerFileName;

        System.out.println(LoggerFile);
    }

}
