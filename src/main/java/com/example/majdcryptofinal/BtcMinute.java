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

public class BtcMinute {

    private IntegerProperty time;
    private FloatProperty open;
    private FloatProperty low;
    private FloatProperty high;
    private FloatProperty close;


    public BtcMinute(int time, float open, float low, float high, float close){
        this.time = new SimpleIntegerProperty(this, "year");
        this.open = new SimpleFloatProperty(this, "open");
        this.low = new SimpleFloatProperty(this, "low");
        this.high = new SimpleFloatProperty(this, "high");
        this.close = new SimpleFloatProperty(this, "close");
        this.setTime(time);
        this.setOpen(open);
        this.setlow(low);
        this.setHigh(high);
        this.setClose(close);
    }

    public int getTime() {  return time.get(); }
    public void setTime(int time) {this.time.set(time);}

    public float getOpen() {  return open.get(); }
    public void setOpen(float _open) {this.open.set(_open);
    }

    public float getlow() {  return low.get(); }
    public void setlow(float _low) {this.low.set(_low);
    }

    public float getHigh() {  return high.get(); }
    public void setHigh(float _high) {this.high.set(_high);
    }

    public float getClose() {  return close.get(); }
    public void setClose(float _close) {this.close.set(_close);
    }


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

//            System.out.println(root);
//            System.out.println(data);
//            System.out.println(element);
//            System.out.println(object);
//            System.out.println(object.get("time"));
            ObservableList<BtcMinute> minValues=FXCollections.observableArrayList();
            for(int i=0;i<=10;i++){
                JsonElement el=data.get(i);
                JsonObject ob=el.getAsJsonObject();

                Integer time=ob.get("time").getAsInt();
                Float open=ob.get("open").getAsFloat();
                Float low=ob.get("low").getAsFloat();
                Float high=ob.get("high").getAsFloat();
                Float close=ob.get("close").getAsFloat();

                minValues.add(new BtcMinute(time,open,low,high,close));

                String fromatted=String.format("Time:%-15d Open:%-15f Low:%-15f High:%-15f Close:%-15f ",
                        time,open,low,high,close);
                System.out.println(fromatted);
            }


//            Set<Map.Entry<String, JsonElement>> entrySet = data.entrySet();
//            for(Map.Entry<String,JsonElement> entry : entrySet){
//                String raw_year = entry.getKey();
//                String year = raw_year.substring(0, 4);
//                String month = raw_year.substring(4, 6);
//                Float value = entry.getValue().getAsFloat();
////                values.add(new BtcMinute(year, month, value));
//            }
            System.out.println();
            return minValues;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
