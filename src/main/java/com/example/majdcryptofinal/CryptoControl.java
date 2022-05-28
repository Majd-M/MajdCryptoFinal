package com.example.majdcryptofinal;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;

import java.util.concurrent.CompletableFuture;

public class CryptoControl {

    @FXML
    private Tab DayTab;

    @FXML
    private Tab HourTab;

    @FXML
    private Tab MinTab;

    @FXML
    public Label currPriceLabel;

    @FXML
    private LineChart<?, ?> dayChart;

    @FXML
    private LineChart<?, ?> hourChart;

    @FXML
    LineChart<Integer, Float> minChart;


    public void doLoad(){
        /* Draw as much as possible before going Async */
        preDraw();
        CompletableFuture<ObservableList<BtcMinute>> future = new CompletableFuture<>();
        future.supplyAsync(this::setupChart).thenAccept(this::drawChart);

    }

    public void preDraw(){
        MinTab.getGraphic();
        minChart.getXAxis().setLabel("time");
        minChart.getYAxis().setLabel("open");
    }

    public XYChart.Series<Integer, Float> setupChart(){

        XYChart.Series<Integer, Float> series = new XYChart.Series<>();
        ObservableList<BtcMinute> values=BtcMinute.getMinute();
        for(BtcMinute bt:values){
            Integer time=bt.getTime();
            Float open=bt.getOpen();
            series.getData().add(new XYChart.Data<Integer,Float>(time,open));
        }

        //*********Using this as placeholder***************
        XYChart.Series<Integer, Float> data=new XYChart.Series<>();
        data.getData().add(new XYChart.Data<Integer,Float>(1, (float) 6789.0009));
        data.getData().add(new XYChart.Data<Integer,Float>(2, (float) 7892.0009));
        data.getData().add(new XYChart.Data<Integer,Float>(3, (float) 890.0009));
        data.getData().add(new XYChart.Data<Integer,Float>(4, (float) 123.0009));
        data.getData().add(new XYChart.Data<Integer,Float>(5, (float) 789.0009));


        return data;
    }

    public void drawChart(XYChart.Series<Integer, Float> series){
        Platform.runLater(()->{
            minChart.getData().setAll(series);
        });
    }
}
