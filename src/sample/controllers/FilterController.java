package sample.controllers;

/**
 * Created by strat on 30.06.15.
 */

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import sample.custom.NumberTextField;

import java.io.IOException;
import java.util.HashMap;

public class FilterController {

    public NumberTextField finishPrice;
    public NumberTextField startPrice;
    public ChoiceBox citiescategory;
    public Button toSearch;
    //    map городов
    private HashMap<String, String> cityMap;

    @FXML
    private void initialize() {
//    заполнение городов
        cityMap = new HashMap<String, String>();
        cityMap.put("Москва", "moskva");
        cityMap.put("Ульяновск", "ulyanovsk");
        cityMap.put("Самара", "samara");
//        заполнение ключами
        citiescategory.setItems(FXCollections.observableArrayList(cityMap.keySet()));
    }

    public void actionSearch(ActionEvent actionEvent) {
//        получаем по ключу необходимое слово(оно выводится в консоль в качестве примера
        String curCity = cityMap.get(citiescategory.getValue().toString());
        System.out.print(curCity);
//        глобальный url для загрузок объявлений(страница)
        MainController.httpQuery = "https://www.avito.ru/" + curCity + "/avtomobili/vaz_lada?pmax=3000000&pmin=300000";
        openMainWindow();
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
