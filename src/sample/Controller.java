package sample;

import data.reader.Instances;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import metas.MetasEnum;

import java.io.File;
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


    static XYChart.Series<Number,Number> series ;


    private static MetasEnum metaActuel ;

    @FXML
    public void initialize() {

        for(MetasEnum meta:MetasEnum.values()){
            metasChoice.getItems().add(meta.name());
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
    public void solve(ActionEvent actionEvent) {
        if(systemBusy) return;
        systemBusy=true;

        series = new XYChart.Series<>();
        //series.setName("Fitness");

        chart.getData().clear();
        chart.getData().add(series);

        Service service = new Service() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        ChartUpdateLifeCycle();
                        metas.Controller.lance(metaActuel);
                        stopUpdateChartLifCycle();
                        return null;
                    }
                };
            }
        };

        service.start();
    }


    private static Timer timer = new Timer();
    private static int sec = 0 ;
    public static void ChartUpdateLifeCycle(){
        metas.Controller.init();
        sec = 0 ;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateChart(sec++,(int)metas.Controller.getCurrentFitness());
            }
        },5,1000);
   }

   public static void stopUpdateChartLifCycle(){
       timer.cancel();
   }

    public static void updateChart(int x,int y) {
        Platform.runLater(() -> {
            series.getData().add(new XYChart.Data<>(x,y));
        });
    }


}

