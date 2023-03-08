package com.alexitto.tablaproductosapi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Locale.setDefault(Locale.getDefault());
        ResourceBundle resourceBundle = ResourceBundle.getBundle("/i18n/spanish");
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("tabla-view.fxml"),resourceBundle);
        Scene scene = new Scene(fxmlLoader.load(), 840, 740);
        stage.setTitle("Tabla Producto Api");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}