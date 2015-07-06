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
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.custom.NumberTextField;
import sample.dbclasses.Category;
import sample.dbclasses.City;
import sample.dbclasses.Filter;
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
    @FXML
    public TextField urlAd;
    public ChoiceBox subcategory;
    JDBCClient jdbcClient;

    public ChoiceBox category;
    //    map городов
    private HashMap<String, String> cityMap;
    private HashMap<String, String> categorMap;
    private HashMap<String, String> subcategorMap = new HashMap<String, String>();

    private boolean isEditFilter = false;
    private int IDEditFilter;


    @FXML
    private void initialize() {
        Filter editFilter = new Filter();
        /*--------------------------------------------*/
        /*Так как нет загрузки, закомментирую код*/
        /*НЕ ТРОГАТЬ!!!!*/
        try {
            if (!jdbcClient.getFilterAll().isEmpty()) {
                editFilter =                 jdbcClient.GetFilterByID(1);
                citiescategory.setItems(FXCollections.observableArrayList(editFilter.getCity()));
                category.setItems(FXCollections.observableArrayList(editFilter.getCategory()));
                subcategory.setItems(FXCollections.observableArrayList(editFilter.getSubcategory()));
                startPrice.setText(String.valueOf(editFilter.getStartPrice()));
                finishPrice.setText(String.valueOf(editFilter.getFilterURL()));
                photocheck.setSelected(editFilter.getIsPhoto());
                urlAd.setText(String.valueOf(editFilter.getFilterURL()));


            } else {
                citiescategory.setItems(FXCollections.observableArrayList(""));
                category.setItems(FXCollections.observableArrayList(""));
                subcategory.setItems(FXCollections.observableArrayList(""));
                startPrice.setText(String.valueOf(""));
                finishPrice.setText(String.valueOf(""));
                photocheck.setSelected(false);
                urlAd.setText("");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }



        categorMap = new HashMap<String, String>();
        try {
            jdbcClient = new JDBCClient();

            loadCategories(jdbcClient);
            loadCities(jdbcClient);

            category.setItems(FXCollections.observableArrayList(categorMap.keySet()));
            category.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                    String parentKey = categorMap.get(observable.getValue().toString()).toString();
                    ArrayList<String> subcatArray = new ArrayList<String>();
                    try {
                        JDBCClient jdbcClient = new JDBCClient();
                        subcategorMap = new HashMap<String, String>();
                        for (Category item : jdbcClient.categorySelectChild(parentKey)) {
                            subcategorMap.put(item.getName(), item.getUrl());
                        }
                        subcategory.setItems(FXCollections.observableArrayList(subcategorMap.keySet()));
                        jdbcClient.closeStatement();
                        jdbcClient.closeConnection();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
            jdbcClient.closeStatement();
            jdbcClient.closeConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        citiescategory.setItems(FXCollections.observableArrayList(cityMap.keySet()));
    }

    public void actionSearch(ActionEvent actionEvent) {
        Stage stageClose = (Stage) toSearch.getScene().getWindow();
        if(urlAd.getText().isEmpty()) {
            try {
                if (citiescategory.getValue() != null) {
                    String categ = "";
                    String curCity = cityMap.get(citiescategory.getValue().toString());
                    if (subcategory.getValue() != null) {
                        categ = "/" + subcategorMap.get(subcategory.getValue().toString());
                    } else if (category.getValue() != null) {
                        categ = "/" + categorMap.get(category.getValue().toString());
                        ;
                    } else {
                        categ = "/";
                    }
                    MainController.httpQuery = "https://www.avito.ru/" + curCity + categ + (photocheck.isSelected() ? "?i=1" : "?") + (finishPrice.getText().equals("") ? "" : "&pmax=" + finishPrice.getText()) + (startPrice.getText().equals("") ? "" : "&pmin=" + startPrice.getText());
/*Добавление фильтра в бд*/
                    Filter filter = new Filter(citiescategory.getValue().toString(),category.getValue().toString(),subcategory.getValue().toString(),
                            Integer.parseInt(startPrice.getText()),Integer.parseInt(finishPrice.getText()),photocheck.isSelected(),urlAd.getText());
                    jdbcClient.filterAdd(filter);
/*-----------------------*/
                     /*Воможность отправить значение в другое окно*/
        //IDEditFilter;
        /*--------------------------------------------*/

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
        else{
            MainController.httpQuery = urlAd.getText();
            System.out.println(MainController.httpQuery);
            openMainWindow(stageClose);
        }
    }

    private void loadCategories(JDBCClient jdbcClient) {
        try {
            for (Category item : jdbcClient.categorySelectChild("1")) {
                categorMap.put(item.getName(), item.getUrl());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadCities(JDBCClient jdbcClient) {
        cityMap = new HashMap<String, String>();
        try {
            for (City item : jdbcClient.getCityAll()) {
                cityMap.put(item.getName(), item.getURL());
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
