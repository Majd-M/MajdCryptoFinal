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
import java.text.DecimalFormat;
import java.text.NumberFormat;
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

    //Day Chart Element
    @FXML
    private LineChart<String, Float> dayChart;
    @FXML
    private CategoryAxis dayDataAxis;
    @FXML
    private NumberAxis dayPriceAxis;

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
    private NumberAxis minPriceAxis;
    @FXML
    private CategoryAxis minDataAxis;


    public void doMinLoad(){
        preDrawMin();
        CompletableFuture<ObservableList<BtcMinute>> minfuture = new CompletableFuture<>();
        minfuture.supplyAsync(this::setupMinChart).thenAccept(this::drawMinChart);
    }

    public void doHourLoad(){
        preDrawHour();
        CompletableFuture<ObservableList<BtcHour>> hourFuture = new CompletableFuture<>();
        hourFuture.supplyAsync(this::setupHourChart).thenAccept(this::drawHourChart);
    }

    public void doDayLoad(){
        preDrawDay();
        CompletableFuture<ObservableList<BtcDay>> dayFuture = new CompletableFuture<>();
        dayFuture.supplyAsync(this::setupDayChart).thenAccept(this::drawDayChart);
    }

    public void preDrawMin(){
        //Setting up the chart properties
        minChart.setAnimated(true);
        minDataAxis.setAnimated(true);
        minDataAxis.setAutoRanging(true);
        minPriceAxis.setAutoRanging(false);
        minPriceAxis.setLowerBound(BtcMinute.minVal-2);
        minPriceAxis.setUpperBound(BtcMinute.maxVal+2);
        minPriceAxis.setTickUnit(5);
        minPriceAxis.setAnimated(true);

    }

    public void preDrawHour(){
        //Setting up the chart properties
        hourPriceAxis.setAutoRanging(false);
        hourPriceAxis.setLowerBound(BtcHour.minVal-20);
        hourPriceAxis.setUpperBound(BtcHour.maxVal+20);
        hourPriceAxis.setTickUnit(5);
        hourPriceAxis.setAnimated(true);
        hourChart.setAnimated(true);
    }

    public void preDrawDay(){
        //Setting up the chart properties
        dayPriceAxis.setAutoRanging(false);
        dayPriceAxis.setLowerBound(BtcDay.minVal-100);
        dayPriceAxis.setUpperBound(BtcDay.maxVal+100);
        dayPriceAxis.setTickUnit(5);
        dayPriceAxis.setAnimated(true);
        dayChart.setAnimated(true);
    }

    public XYChart.Series<String, Float> setupMinChart(){
        //XY series to be passed to the chart later
        NumberFormat formatter=new DecimalFormat("#0.0");
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
            sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT-8"));
            String formattedDate = sdf.format(date);

            time=formattedDate;
            int timeToDiv=parseInt(time);       //converstion of the time to a float
            float firstTime=(float) timeToDiv;  //float casted
            nextTime=firstTime+1;         //Used to divide the time and plot Low & high

            for(int i=0;i<3;i++){
                String chartTime=String.valueOf(formatter.format(firstTime));
                Float value=prices[i];
//                System.out.println(String.format("%-5s %-10f",chartTime,value));
                series.getData().add(new XYChart.Data<String, Float>(chartTime, value));
                firstTime+=0.1;
            }
        }
//        series.getData().add(new XYChart.Data<String, Float>(String.valueOf(nextTime),BtcMinute.lastVal));
        return series;
    }

    public XYChart.Series<String, Float> setupHourChart(){
        //XY series to be passed to the chart later
//        System.out.println("HOUR DATA:");
        XYChart.Series<String, Float> series = new XYChart.Series<>();
        ObservableList<BtcHour> values=BtcHour.getHour();
        NumberFormat formatter=new DecimalFormat("#0.0");
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
                String chartTime=String.valueOf(formatter.format(firstTime));
                Float value=prices[i];
//                System.out.println(String.format("%-5s %-10f", chartTime, value));
                series.getData().add(new XYChart.Data<String, Float>(chartTime, value));
                firstTime+=0.1;
            }

        }
        series.getData().add(new XYChart.Data<String, Float>(String.valueOf(nextTime),BtcHour.lastVal));
        return series;
    }

    public XYChart.Series<String, Float> setupDayChart(){
        //XY series to be passed to the chart later
        XYChart.Series<String, Float> series = new XYChart.Series<>();
        ObservableList<BtcDay> values=BtcDay.getDay();
        NumberFormat formatter=new DecimalFormat("#0.0");
        float nextTime=0;
        for(BtcDay bt:values){
            String time=bt.getTime();
            float prices[]={bt.getOpen(), bt.getLow(), bt.getHigh(), bt.getClose()};

            //Converting Unix time to Minutes for dsiplay and data plotting
            int unixHours=parseInt(time);
            Date date = new java.util.Date(unixHours*1000L);
            SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd");
            sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT-8"));
            String formattedDate = sdf.format(date);

            time=formattedDate;

            int timeToDiv=parseInt(time);       //conversion of the time to a float
            float firstTime=(float) timeToDiv;  //float casted
            nextTime=firstTime+1;         //Used to divide the time and plot Low & high

            for(int i=0;i<3;i++){
                String chartTime=String.valueOf(formatter.format(firstTime));
                Float value=prices[i];
//                System.out.println(String.format("%-5s %-10f", chartTime, value));
                series.getData().add(new XYChart.Data<String, Float>(chartTime, value));
                firstTime+=0.1;
            }

        }
//        series.getData().add(new XYChart.Data<String, Float>(String.valueOf(nextTime),BtcHour.lastVal));
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

    public void drawDayChart(XYChart.Series<String, Float> series){
        Platform.runLater(()->{
            //Set the Current time
            String priceFormat=String.format("$%-10s",String.valueOf(BtcMinute.lastVal));
            currPriceLabel.setText(priceFormat);
            dayChart.getData().setAll(series);
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        BtcMinute.getMinute();
        BtcHour.getHour();
        BtcDay.getDay();
        preDrawMin();
        preDrawHour();
        doMinLoad();
        doHourLoad();
        doDayLoad();

    }
}
