package com.example.majdcryptofinal;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

import static java.lang.Integer.parseInt;

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
    public LineChart<String, Float> minChart;

    @FXML
    private NumberAxis priceAxis;

    @FXML
    private CategoryAxis dataAxis;


    @FXML
    public void doLoad(){
        /* Draw as much as possible before going Async */
        preDraw();
        CompletableFuture<ObservableList<BtcMinute>> future = new CompletableFuture<>();
        future.supplyAsync(this::setupChart).thenAccept(this::drawChart);
    }

    public void preDraw(){
        priceAxis.setAutoRanging(false);
        priceAxis.setLowerBound(BtcMinute.minVal-5);
        priceAxis.setUpperBound(BtcMinute.maxVal+5);
        dataAxis.toNumericValue("d");
        minChart.setAnimated(true);
    }

    public XYChart.Series<String, Float> setupChart(){

        XYChart.Series<String, Float> series = new XYChart.Series<>();
        ObservableList<BtcMinute> values=BtcMinute.getMinute();
        for(BtcMinute bt:values){

            String time=bt.getTime();
            float prices[]={bt.getOpen(), bt.getLow(), bt.getHigh(), bt.getClose()};

            //Converting Unix time
            int unixMinutes=parseInt(time);
            Date date = new java.util.Date(unixMinutes*1000L);
            SimpleDateFormat sdf = new java.text.SimpleDateFormat("mm");
            sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+8"));
            String formattedDate = sdf.format(date);

            time=formattedDate;


            int timeToDiv=parseInt(time);
            float firstTime=(float) timeToDiv;
            float nextTime=firstTime+1;


            int j=0;
            while(firstTime<nextTime){
                String chartTime=String.valueOf(firstTime);
                Float value=prices[j];
                System.out.println(String.format("%-5s %-10f",chartTime,value));
                series.getData().add(new XYChart.Data<String, Float>(chartTime, value));
                firstTime+=0.25;
                j+=1;
            }
        }

        return series;
    }

    public void drawChart(XYChart.Series<String, Float> series){
        Platform.runLater(()->{
            String priceFormat=String.format("$%-10s",String.valueOf(BtcMinute.lastVal));
            currPriceLabel.setText(priceFormat);
            minChart.getData().setAll(series);
        });


    }


}
