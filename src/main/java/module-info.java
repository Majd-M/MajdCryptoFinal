module com.example.majdcryptofinal {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.majdcryptofinal to javafx.fxml;
    exports com.example.majdcryptofinal;
}