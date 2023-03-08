package com.alexitto.tablaproductosapi.controller;

import com.alexitto.tablaproductosapi.models.Product;
import javafx.application.Platform;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class Controller {

    private final int OK = 200;


    @FXML
    public TableView<Product> tableId;
    @FXML
    public TableColumn<Product, ?> columnId;
    @FXML
    public TableColumn<Product, ?> columnCategory;
    @FXML
    public TableColumn<Product, ?> columnName;
    @FXML
    public TableColumn<Product, ?> ColumnPrecio;
    @FXML
    public Button buttonRecargar;
    @FXML
    public PieChart grapicaId;
    @FXML
    public TextField textFieldCategoria;
    @FXML
    public TextField textFieldNombre;
    @FXML
    public TextField textFieldPrecio;
    @FXML
    public TextArea textAreaDescripcion;
    @FXML
    public ImageView imagenProduct;
    @FXML
    public Text loading;

    private ObservableList<Product> list;

    @FXML
    public void initialize() {
        tableId.setEditable(true);
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        ColumnPrecio.setCellValueFactory(new PropertyValueFactory<>("price"));

        tableId.getSelectionModel().selectedItemProperty().addListener((product, oldVal, newVal) -> {
                    if (newVal != null) {
                        textFieldCategoria.setText(newVal.getCategory());
                        textFieldNombre.setText(newVal.getName());
                        textFieldPrecio.setText(String.valueOf(newVal.getPrice()));
                        textAreaDescripcion.setText(newVal.getDescription());
                        loadImage(newVal.getImage());
                    }
                }

        );
        list = FXCollections.observableArrayList();
    }

    private void loadImage(String image) {
        new Thread(() -> {
            imagenProduct.setImage(null);
            loading.setText("Loading image .....");
            imagenProduct.setImage(new Image(image));
            loading.setText("");
        }).start();
    }


    @FXML
    private void refreshTable() {
        new Thread(() -> {

            buttonRecargar.setDisable(true);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://fakestoreapi.com/products")).build();
            HttpResponse<String> response;
            try {
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (response.statusCode() == OK) {
                fillTable(response);
                loadPieChart();

                Platform.runLater(() -> {
                    buttonRecargar.setDisable(false);
                });
            }


        }).start();


    }

    private void loadPieChart() {
        ObservableList<PieChart.Data> listPieChart = FXCollections.observableArrayList(

                new PieChart.Data("Jewelary", list.stream().map(Product::getCategory).filter("jewelery"::equals).count()),
                new PieChart.Data("Electronics", list.stream().map(Product::getCategory).filter("electronics"::equals).count()),
                new PieChart.Data("Men's clothing", list.stream().map(Product::getCategory).filter("men's clothing"::equals).count()),
                new PieChart.Data("Women's clothing", list.stream().map(Product::getCategory).filter("women's clothing"::equals).count())

        );

        Platform.runLater(() -> {
            grapicaId.setData(listPieChart);

        });

    }


    private void fillTable(HttpResponse<String> response) {
        JSONArray json = new JSONArray(response.body());
        for (int i = 0; i < json.length(); i++) {
            JSONObject object = json.getJSONObject(i);
            Product product = new Product(
                    object.getInt("id"),
                    object.getString("title"),
                    object.getString("category"),
                    object.getString("image"),
                    object.getString("description"),
                    object.getDouble("price")
            );
            list.add(product);
        }
        tableId.setItems(list);

    }


}