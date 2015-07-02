package sample.controllers;

/**
 * Created by strat on 30.06.15.
 */

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import sample.Main;
import sample.custom.NumberTextField;
import sample.dbclasses.Category;
import sample.dbclasses.JDBCClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class FilterController {

    @FXML
    public NumberTextField finishPrice;
    @FXML
    public NumberTextField startPrice;
    @FXML
    public ChoiceBox citiescategory;
    @FXML
    public Button toSearch;
    @FXML
    public CheckBox photocheck;

    public ChoiceBox subcategory;

    public ChoiceBox category;
    //    map городов
    private HashMap<String, String> cityMap;
    private HashMap<String, String> categorMap;
    private HashMap<String, String> subcategorMap;
    private HashMap<String, String> subcategorMap_ = new HashMap<String, String>();



    @FXML
    private void initialize() {
        JDBCClient jdbcClient = Main.jdbcClient;
        cityMap = Main.citys;
        citiescategory.setItems(FXCollections.observableArrayList(cityMap.keySet()));
        categorMap = Main.categories_;
        category.setItems(FXCollections.observableArrayList(categorMap.keySet()));
        subcategorMap = Main.subcategories_;

        /*////////////////*/
        category.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                //System.out.println(               categorMap.get(observable.getValue().toString()).toString());
                categorMap.get(observable.getValue().toString()).toString();

                for (String title : subcategorMap.keySet()) {
                    if (title.equals(observable.getValue().toString())) {
                        subcategorMap_.put(title,"");
                    }
                }
                System.out.println(               categorMap.get(observable.getValue().toString()).toString());

                subcategory.setItems(FXCollections.observableArrayList(subcategorMap_.keySet()));

            }
        });

    }

    public void actionSearch(ActionEvent actionEvent) {
        try {
            if (citiescategory.getValue() != null) {
                String curCity = cityMap.get(citiescategory.getValue().toString());
                MainController.httpQuery = "https://www.avito.ru/" + curCity + "/avtomobili?" + (photocheck.isSelected() ? "i=1" : "") + (finishPrice.getText().equals("") ? "" : "&pmax=" + finishPrice.getText()) + (startPrice.getText().equals("") ? "" : "&pmin=" + startPrice.getText());
                System.out.println(MainController.httpQuery);
                openMainWindow();
            }
        }catch (Exception e){
            System.out.println("Сылка немного не правильная, но я всеравно все покажу :))");
        }
    }

    private void openMainWindow() {
        Stage stageClose = (Stage) toSearch.getScene().getWindow();
        Parent parent = null;
        try {
            parent = FXMLLoader.load(getClass().getResource("/sample/view/main.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setTitle("Main");
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        stageClose.close();
    }
}
