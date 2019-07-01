package sample;

import data.Logger;
import data.reader.Instances;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import metas.MetasEnum;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {

    private static String LocalPath ;
    private static String BenchMarksPath ;

    static {
        LocalPath = new java.io.File(".").getPath()+"/bench_marks/100/";
        BenchMarksPath = LocalPath;
    }

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

    @FXML
    public Spinner limitationTimeSpiner;

    @FXML
    public CheckBox limitationTime;

    private static MetasEnum metaActuel ;


    final DirectoryChooser directoryChooser = new DirectoryChooser();


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


        directoryChooser.setTitle("choisissez le dossier du benchmarks");
        directoryChooser.setInitialDirectory(new java.io.File("."));

        instanceChoice.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> observableValue, Number number, Number number2) -> {
            afficheInstance(
                    instanceChoice.getItems().get((Integer) number2)
            );
        });

        limitationTimeSpiner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0,5000)
        );

        limitationTimeSpiner.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            try{
                // parsingTest
                Integer.parseInt(newValue);
            }catch (Exception e) {
                limitationTimeSpiner.getEditor().setText(oldValue);
            }
        });

        limitationTimeSpiner.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == false) {
                limitationTimeSpiner.increment(0);
            }
        });

        limitationTime.selectedProperty().addListener((observable, oldValue, newValue) -> {
            limitationTimeSpiner.setDisable(!newValue);
        });

        series = new XYChart.Series<>();
        //series.setName("Fitness");

        chart.getData().clear();
        chart.getData().add(series);
    }

    private void afficheInstance(String instance) {
        try{

            Logger.initPersistanceLogs(instance);

            String path = BenchMarksPath+"//"+instance;

            new Instances(path);

            instanceContiant.setText(Instances.instanceString);
        }catch (Exception e){instanceChoice.getItems().clear();}
    }

    @FXML
    private void chooseFile(){
        update_Benchmarks(directoryChooser.showDialog(new Stage()));
    }

    private void update_Benchmarks(File repo){
        if(repo == null) return;
        try{
            BenchMarksPath = repo.getPath();
            instanceChoice.getItems().clear();
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
        }catch (Exception e){instanceChoice.getItems().clear();}
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

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(metas.Controller.isStopped()) return;
                System.out.println("Compared Value ---> "+Integer.parseInt(limitationTimeSpiner.getValue().toString()));
                if(limitationTime.isSelected() && Integer.parseInt(limitationTimeSpiner.getValue().toString()) < sec){
                    Platform.runLater( () -> {
                        stopSolv();
                    });
                    return;
                }else{
                    updateChart(sec++,(int)metas.Controller.getCurrentFitness());
                }
            }
        },999,1000);
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

