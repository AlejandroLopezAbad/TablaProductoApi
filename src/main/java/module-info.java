module com.alexitto.tablaproductosapi {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires org.json;



    opens com.alexitto.tablaproductosapi to javafx.fxml;
    exports com.alexitto.tablaproductosapi;
    exports com.alexitto.tablaproductosapi.controller;
    opens com.alexitto.tablaproductosapi.controller to javafx.fxml;
    opens com.alexitto.tablaproductosapi.models to javafx.fxml;
    exports com.alexitto.tablaproductosapi.models;
}