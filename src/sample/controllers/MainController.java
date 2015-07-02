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
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainController {

    protected static Stage stageMain;
    protected static String httpQuery;
    @FXML
    private Button changeFilter;
    @FXML
    private Button addFilter;
    @FXML
    private ListView<AvitoAd> listView;
    private ObservableList observableList;

    public MainController(){
        observableList = FXCollections.observableArrayList();
    }
    @FXML
    private void initialize() {
        setListView();
        ExecutorService service = Executors.newCachedThreadPool();
        service.submit(new Runnable() {
            @Override
            public void run() {
                download();
            }
        });

    }
    private void download() {
        AvitoApi avitoApi = new AvitoApi();
        try {
            for (AvitoAd ad : avitoApi.getAdsFromRawQueryYield(httpQuery)) {
                observableList.add(ad);

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
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
}
