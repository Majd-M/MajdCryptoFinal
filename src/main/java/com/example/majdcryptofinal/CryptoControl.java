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

public class CryptoControl implements Initializable{
    //Tabs
    @FXML
    private Tab DayTab;
    @FXML
    private Tab HourTab;
    @FXML
    private Tab MinTab;

    @FXML
    public Label currPriceLabel;

    @FXML
    private LineChart<String, Float> dayChart;

    //Hour Chart Elements
    @FXML
    private LineChart<String, Float> hourChart;
    @FXML
    private NumberAxis hourPriceAxis;
    @FXML
    private CategoryAxis hourdataAxis;

    //Minute Chart Elements
    @FXML
    public LineChart<String, Float> minChart;
    @FXML
    private NumberAxis priceAxis;
    @FXML
    private CategoryAxis dataAxis;



    public void doMinLoad(){
        preDrawMin();
        CompletableFuture<ObservableList<BtcMinute>> minfuture = new CompletableFuture<>();
        minfuture.supplyAsync(this::setupMinChart).thenAccept(this::drawMinChart);
    }
    public void doHourLoad(){
        preDrawHour();
        CompletableFuture<ObservableList<BtcHour>> hourfuture = new CompletableFuture<>();
        hourfuture.supplyAsync(this::setupHourChart).thenAccept(this::drawHourChart);
    }

    public void preDrawMin(){
        //Setting up the chart properties
        priceAxis.setAutoRanging(false);
        priceAxis.setLowerBound(BtcMinute.minVal-5);
        priceAxis.setUpperBound(BtcMinute.maxVal+5);
        priceAxis.setTickUnit(5);
        dataAxis.setAnimated(true);
        minChart.setAnimated(true);
    }

    public void preDrawHour(){
        //Setting up the chart properties
        hourdataAxis.toNumericValue("d");
        hourPriceAxis.setAutoRanging(false);
        hourPriceAxis.setLowerBound(BtcHour.minVal-20);
        hourPriceAxis.setUpperBound(BtcHour.maxVal+20);
        hourPriceAxis.setTickUnit(5);
        hourPriceAxis.setAnimated(true);
        minChart.setAnimated(true);
    }

    public XYChart.Series<String, Float> setupMinChart(){
        //XY series to be passed to the chart later
        XYChart.Series<String, Float> series = new XYChart.Series<>();
        ObservableList<BtcMinute> values=BtcMinute.getMinute();
        float nextTime=0;
        for(BtcMinute bt:values){
            String time=bt.getTime();
            float prices[]={bt.getOpen(), bt.getLow(), bt.getHigh(), bt.getClose()};

            //Converting Unix time to Minutes for dsiplay and data plotting
            int unixMinutes=parseInt(time);
            Date date = new java.util.Date(unixMinutes*1000L);
            SimpleDateFormat sdf = new java.text.SimpleDateFormat("mm");
            sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+8"));
            String formattedDate = sdf.format(date);

            time=formattedDate;
            int timeToDiv=parseInt(time);       //converstion of the time to a float
            float firstTime=(float) timeToDiv;  //float casted
            nextTime=firstTime+1;         //Used to divide the time and plot Low & high

            for(int i=0;i<3;i++){
                String chartTime=String.valueOf(firstTime);
                Float value=prices[i];
//                System.out.println(String.format("%-5s %-10f",chartTime,value));
                series.getData().add(new XYChart.Data<String, Float>(chartTime, value));
                firstTime+=0.3;
            }
        }
        series.getData().add(new XYChart.Data<String, Float>(String.valueOf(nextTime),BtcMinute.lastVal));
        return series;
    }

    public XYChart.Series<String, Float> setupHourChart(){
        //XY series to be passed to the chart later
//        System.out.println("HOUR DATA:");
        XYChart.Series<String, Float> series = new XYChart.Series<>();
        ObservableList<BtcHour> values=BtcHour.getHour();
        float nextTime=0;
        for(BtcHour bt:values){
            String time=bt.getTime();
            float prices[]={bt.getOpen(), bt.getLow(), bt.getHigh(), bt.getClose()};

            //Converting Unix time to Minutes for dsiplay and data plotting
            int unixHours=parseInt(time);
            Date date = new java.util.Date(unixHours*1000L);
            SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH");
            sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT-8"));
            String formattedDate = sdf.format(date);

            time=formattedDate;

            int timeToDiv=parseInt(time);       //converstion of the time to a float
            float firstTime=(float) timeToDiv;  //float casted
            nextTime=firstTime+1;         //Used to divide the time and plot Low & high

            for(int i=0;i<3;i++){
                String chartTime=String.valueOf(firstTime);
                Float value=prices[i];
                System.out.println(String.format("%-5s %-10f", chartTime, value));
                series.getData().add(new XYChart.Data<String, Float>(chartTime, value));
                firstTime+=0.3;
            }

        }
        series.getData().add(new XYChart.Data<String, Float>(String.valueOf(nextTime),BtcHour.lastVal));
        return series;
    }

    public void drawMinChart(XYChart.Series<String, Float> series){
        Platform.runLater(()->{
            //Set the Current time
            String priceFormat=String.format("$%-10s",String.valueOf(BtcMinute.lastVal));
            currPriceLabel.setText(priceFormat);
            minChart.getData().setAll(series);
        });
    }

    public void drawHourChart(XYChart.Series<String, Float> series){
        Platform.runLater(()->{
            //Set the Current time
            String priceFormat=String.format("$%-10s",String.valueOf(BtcMinute.lastVal));
            currPriceLabel.setText(priceFormat);
            hourChart.getData().setAll(series);
        });
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        preDrawMin();
        preDrawHour();
        doMinLoad();
        doHourLoad();
    }
}
