package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import sample.api.AvitoAd;
import sample.api.AvitoApi;

import java.io.IOException;

public class MainController {

    public Button changeFilter;
    public Button addFilter;
    @FXML
    private ListView<AvitoAd> listView;
    private ObservableList observableList;

    public MainController(){
        observableList = FXCollections.observableArrayList();
    }
    @FXML
    private void initialize() {
        setListView();
    }
    public void setListView(){

        AvitoApi avitoApi = new AvitoApi();
        try {
            observableList.setAll(avitoApi.getAdsFromRawQuery("https://www.avito.ru/ulyanovsk/avtomobili/vaz_lada?i=1&pmax=3000000&pmin=90000"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        listView.setItems(observableList);
        listView.setCellFactory(param -> new ListViewCell());
    }

    public void actionChangeFilter(ActionEvent actionEvent) {
        Stage stageClose = (Stage) changeFilter.getScene().getWindow();
        Parent parent = null;
        try {
            parent = FXMLLoader.load(getClass().getResource("/sample/view/filter.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setTitle("Filter");
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        stageClose.close();
    }

    public void actionAddFilter(ActionEvent actionEvent) {

    }
}
