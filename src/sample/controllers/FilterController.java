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
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Main;
import sample.custom.NumberTextField;
import sample.dbclasses.Category;
import sample.dbclasses.JDBCClient;

import java.io.IOException;
import java.sql.SQLException;
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
    private HashMap<String, String> subcategorMap = new HashMap<String, String>();

    @FXML
    private void initialize() {
        JDBCClient jdbcClient = Main.jdbcClient;
        cityMap = Main.citys;
        citiescategory.setItems(FXCollections.observableArrayList(cityMap.keySet()));

        categorMap = Main.categories_;
        category.setItems(FXCollections.observableArrayList(categorMap.keySet()));

        category.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                //System.out.println(               categorMap.get(observable.getValue().toString()).toString());
                String parentKey = categorMap.get(observable.getValue().toString()).toString();
                ArrayList<String> subcatArray = new ArrayList<String>();
                try {
                    subcategorMap = new HashMap<String, String>();
                    for (Category item : jdbcClient.categorySelectChild(parentKey)) {
                        subcategorMap.put(item.getName(), item.getURL());
                    }
                    subcategory.setItems(FXCollections.observableArrayList(subcategorMap.keySet()));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void actionSearch(ActionEvent actionEvent) {
        Stage stageClose = (Stage) toSearch.getScene().getWindow();
        try {
            if (citiescategory.getValue() != null) {
                String categ ="";
                String curCity = cityMap.get(citiescategory.getValue().toString());
                if(subcategory.getValue() != null){
                    categ = "/" + subcategorMap.get(subcategory.getValue().toString());
                }
                else if(category.getValue() != null){
                    categ = "/" + categorMap.get(category.getValue().toString());;
                }
                else{
                    categ = "/";
                }
                MainController.httpQuery = "https://www.avito.ru/" + curCity + categ + (photocheck.isSelected() ? "?i=1" : "?") + (finishPrice.getText().equals("") ? "" : "&pmax=" + finishPrice.getText()) + (startPrice.getText().equals("") ? "" : "&pmin=" + startPrice.getText());
                System.out.println(MainController.httpQuery);
                openMainWindow(stageClose);
            } else {
                final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(stageClose);
                VBox dialogVbox = new VBox(20);
                dialogVbox.getChildren().add(new Text("Выберете город"));
                Scene dialogScene = new Scene(dialogVbox, 150, 50);
                dialog.setScene(dialogScene);
                dialog.show();
            }
        } catch (Exception e) {
            System.out.println("Сылка немного не правильная, но я всеравно все покажу :))");
        }
    }

    private void openMainWindow(Stage stageClose) {
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
