package sample.controllers;

import javafx.collections.FXCollections;
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
import sample.*;
import sample.api.AvitoAd;
import sample.listviewcell.ListViewCell;

import java.io.IOException;
import java.util.Comparator;

public class MainController {

    public Button favorites;
    @FXML
    private Button changeFilter;
    @FXML
    private Button addFilter;
    @FXML
    private ListView<AvitoAd> listView;


    private App app;

    public void setApp(App app) {
        this.app = app;
        SortedList<AvitoAd> sortedList  = new SortedList<AvitoAd>(app.getAdsObservableList(), new Comparator<AvitoAd>() {
            @Override
            public int compare(AvitoAd o1, AvitoAd o2) {

                return - o1.getDateTime().compareTo(o2.getDateTime()) ;
            }
        });
        setListView(sortedList);
    }

    public MainController(){ }
    @FXML
    private void initialize() {
    }

    public void setListView(SortedList sortedList){
        listView.setItems(sortedList);
        listView.setCellFactory(param -> new ListViewCell());
    }

    public void actionChangeFilter(ActionEvent actionEvent) {

        app.openFilterWindow();
        /*Stage stageClose = (Stage) changeFilter.getScene().getWindow();
        Parent parent = null;
        try {
            parent = FXMLLoader.load(getClass().getResource("/sample/view/filter.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setTitle("Filter");
            stage.show();
//Возможность передать значения как на Android'e
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        stageClose.close();*/
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
