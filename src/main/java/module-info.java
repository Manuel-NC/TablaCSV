module com.example.tablacsv {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.tablacsv to javafx.fxml;
    exports com.example.tablacsv;
}