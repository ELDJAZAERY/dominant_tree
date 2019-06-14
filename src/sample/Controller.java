package sample;

import data.reader.Instances;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.util.Duration;
import metas.MetasEnum;

import java.io.File;
import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {

    private static String LocalPath = "bench_marks/100/";

    @FXML
    private ChoiceBox<String> instanceChoice;

    @FXML
    private ChoiceBox<String> metasChoice;


    @FXML
    private TextArea instanceContiant ;


    @FXML
    public LineChart<Number, Number> chart;


    @FXML
    public Button solver ;

    @FXML
    public Button resumer ;

    static XYChart.Series<Number,Number> series ;

    @FXML
    public Label timeEnSec;

    @FXML
    public Label fitnessLabel;

    private static MetasEnum metaActuel ;

    @FXML
    public void initialize() {

        fitnessLabel.setText("0");
        timeEnSec.setText("0 Sec");

        for(MetasEnum meta:MetasEnum.values()){
            metasChoice.getItems().add(meta.name());
        }


        for(MetasEnum meta:MetasEnum.values()){
            metasChoice.setValue(meta.name());
            metaActuel = meta;
            break;
        }

        metasChoice.getSelectionModel().selectedIndexProperty().addListener(
            (ObservableValue<? extends Number> observableValue, Number number, Number number2) -> {
                metaActuel = MetasEnum.valueOf(metasChoice.getItems().get((Integer) number2));
        });


        File repo = new File(LocalPath);
        if (repo.isDirectory()) {
            File[] fileList = repo.listFiles();
            for (File f : fileList) {
                instanceChoice.getItems().add(f.getName());
            }

            if(fileList.length > 0) {
                instanceChoice.setValue(fileList[0].getName());
                afficheInstance(fileList[0].getName());
            }
        }
        instanceChoice.getItems().add("BenchMarks externe");

        instanceChoice.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> observableValue, Number number, Number number2) -> {
            afficheInstance(
                    instanceChoice.getItems().get((Integer) number2)
            );
        });

    }


    private void afficheInstance(String instance) {

        String path = "bench_marks\\100\\"+instance;
        new Instances(path);

        instanceContiant.setText(Instances.instanceString);
    }


    private boolean systemBusy = false;

    @FXML
    public void solve() {
        if(systemBusy) return;
        systemBusy=true;


        solver.setDisable(true);
        resumer.setDisable(false);
        fitnessLabel.setText("0");
        timeEnSec.setText("0 Sec");

        series = new XYChart.Series<>();
        //series.setName("Fitness");

        chart.getData().clear();
        chart.getData().add(series);

        Main.service = new Service() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        System.out.println(" ----> new Call <----");
                        ChartUpdateLifeCycle();
                        metas.Controller.lance(metaActuel,this);
                        stopUpdateChartLifCycle();
                        systemBusy=false;
                        solver.setDisable(false);
                        return null;
                    }
                };
            }
        };

        Main.service.start();
        System.out.println(" ---------- \n\n\n  Started  \n\n\n ----------");
        //stopSolv();
        //service.cancel();

    }

    @FXML
    public void stopSolving(){
        stopSolv();
    }

    private void stopSolv(){
        if(Main.service != null ){
            System.out.println(" ---------- \n\n\n  Stopped  \n\n\n ----------");
            Main.service.cancel();
            Main.service = null;

            resumer.setDisable(true);
        }
    }



    private static Timer timer = new Timer();
    private static int sec = 0 ;

    public void ChartUpdateLifeCycle(){
        metas.Controller.init();
        sec = 0 ;
        timer = new Timer();
        updateChart(0,0);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(metas.Controller.isStoped()) return;
                updateChart(sec++,(int)metas.Controller.getCurrentFitness());
            }
        },5,1000);
   }

   public static void stopUpdateChartLifCycle(){
       timer.cancel();
   }

    public void updateChart(int x,int y) {
        Platform.runLater(() -> {
            timeEnSec.setText(x+" Sec");
            fitnessLabel.setText(""+y);
            series.getData().add(new XYChart.Data<>(x,y));
        });
    }

}

