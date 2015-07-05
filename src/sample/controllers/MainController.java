package sample.controllers;

import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import sample.Main;
import sample.api.AvitoAd;
import sample.listviewcell.ListViewCell;

import java.io.IOException;

public class MainController {

    protected static Stage stageMain;
    protected static String httpQuery;
    public Button favorites;
    @FXML
    private Button changeFilter;
    @FXML
    private Button addFilter;
    @FXML
    private ListView<AvitoAd> listView;
    private ObservableList observableList;

    public MainController(){
        observableList = Main.adsObservableList;
    }
    @FXML
    private void initialize() {
        setListView();
    }

    public void setListView(){
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
/*Возможность передать значения как на Android'e*/
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        stageClose.close();
    }

    public void actionAddFilter(ActionEvent actionEvent) {
        Stage stageClose = (Stage) addFilter.getScene().getWindow();
        Parent parent = null;
        try {
            parent = FXMLLoader.load(getClass().getResource("/sample/view/newfilter.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setTitle("AddFilter");
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        stageClose.close();
    }

    public void actionFavorites(ActionEvent actionEvent) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(getClass().getResource("/sample/view/favorites.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setTitle("Favorites");
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
