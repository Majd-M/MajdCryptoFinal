package com.example.majdcryptofinal;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;

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
    private LineChart<?, ?> minCHart;

}
