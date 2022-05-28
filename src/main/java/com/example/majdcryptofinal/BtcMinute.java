package com.example.majdcryptofinal;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BtcMinute {
    //Properties
    private StringProperty time;
    private FloatProperty open;
    private FloatProperty low;
    private FloatProperty high;
    private FloatProperty close;
    public static float minVal;
    public static float maxVal;
    public static float lastVal;

    //Constructor
    public BtcMinute(String time, float open, float low, float high, float close){
        this.time = new SimpleStringProperty(this, "year");
        this.open = new SimpleFloatProperty(this, "open");
        this.low = new SimpleFloatProperty(this, "low");
        this.high = new SimpleFloatProperty(this, "high");
        this.close = new SimpleFloatProperty(this, "close");
        this.setTime(time);
        this.setOpen(open);
        this.setLow(low);
        this.setHigh(high);
        this.setClose(close);
    }

    //Getters and setters
    public String getTime() {  return time.get(); }
    public void setTime(String time) {this.time.set(time);}

    public float getOpen() {  return open.get(); }
    public void setOpen(float _open) {this.open.set(_open);
    }

    public float getLow() {  return low.get(); }
    public void setLow(float _low) {this.low.set(_low);
    }

    public float getHigh() {  return high.get(); }
    public void setHigh(float _high) {this.high.set(_high);
    }

    public float getClose() {  return close.get(); }
    public void setClose(float _close) {this.close.set(_close);
    }

    //Method invoked to return BtcMinute objects
    public static ObservableList<BtcMinute> getMinute(){
        String addr = "https://min-api.cryptocompare.com/data/histominute?aggregate=1&e=CCCAGG&extraParams=CryptoCompare&fsym=BTC&limit=10&tryConversion=false&tsym=USD";

        try {
            URL address = new URL(addr);
            JsonReader reader = new JsonReader(new InputStreamReader(address.openStream()));
            Gson gson = new Gson();
            JsonObject root = gson.fromJson(reader, JsonObject.class);
            JsonArray data = root.getAsJsonArray("Data");
            JsonElement element= data.get(0);
            JsonObject object=element.getAsJsonObject();
            //priceComp is used to get the max, min and last values for the chart
            List<Float> priceComp=new ArrayList<>();
            //The Observable list to be returned by the function
            ObservableList<BtcMinute> minValues=FXCollections.observableArrayList();

            //Iterating through the Json objects
            for(int i=0;i<=10;i++){
                JsonElement el=data.get(i);
                JsonObject ob=el.getAsJsonObject();

                String time=ob.get("time").getAsString();
                Float open=ob.get("open").getAsFloat();
                Float low=ob.get("low").getAsFloat();
                Float high=ob.get("high").getAsFloat();
                Float close=ob.get("close").getAsFloat();

                //Adding values to the Observable lIst
                minValues.add(new BtcMinute(time,open,low,high,close));

                //Adding Values to the price comparer list
                priceComp.add(open);
                priceComp.add(low);
                priceComp.add(high);
                priceComp.add(close);

                //for Debugging
                String fromatted=String.format("Time:%-15s Open:%-15f Low:%-15f High:%-15f Close:%-15f ",
                        time,open,low,high,close);
                System.out.println(fromatted);
            }
            //Assigning the limits for use in the chart and ranging
            minVal=Collections.min(priceComp);
            maxVal= Collections.max(priceComp);
            lastVal=priceComp.get(priceComp.size()-1);
            System.out.println(String.format("Min: %-10f Max: %-10f LastPrice: %-10f",minVal,maxVal,lastVal));

            return minValues;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
