package sample.controllers;

/**
 * Created by strat on 30.06.15.
 */

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.App;
import javafx.scene.control.*;
import sample.custom.NumberTextField;
import sample.dbclasses.Category;
import sample.dbclasses.City;
import sample.dbclasses.JDBCClient;
import sample.models.Filter;

import java.sql.SQLException;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//Переделать нахуй, полный пиздец
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

    public ChoiceBox category;
    private HashMap<String, String> cityMap;
    private HashMap<String, String> categorMap;
    private HashMap<String, String> subcategorMap = new HashMap<String, String>();
    private TreeMap<String, String> sortedCityMap;
    private TreeMap<String, String> sortedCategorMap;
    private TreeMap<String, String> sortedSubcategorMap;

    private App app;

    public void setApp(App app) {
        this.app = app;
        setView();
    }

    private void setView() {
        Filter filter = app.getFilter();
        ArrayList<Category> allCategories = new ArrayList<>();
        try {
            JDBCClient client = new JDBCClient();
            allCategories = client.getCategoryAll();
            client.closeConnection();
        } catch (Exception e) {
        }

        if (filter != null) {
//            urlAd.setText(filter.toRawQuery());
            finishPrice.setText(filter.getMaxPrice() > 0 ? filter.getMaxPrice() + "" : "");
            startPrice.setText(filter.getMinPrice() > 0 ? filter.getMinPrice() + "" : "");
            photocheck.setSelected(filter.isOnlyWithPhoto());

            String city_value = filter.getCity() != null ? filter.getCity() : "";

            for (Map.Entry<String, String> c : cityMap.entrySet()) {
                if (c.getValue().equals(city_value)) {
                    citiescategory.setValue(c.getKey());
                }
            }
            String category_value = filter.getCategory() != null ? filter.getCategory() : "";
            Category c = allCategories.stream().filter(x -> x.getUrl().equals(category_value)).findFirst().orElse(null);

            if (c != null) {
                if (c.getParent().equals("1")) {
                    category.setValue(c.getName());
                } else {
                    Category p = allCategories.stream().filter(x -> x.getUrl().equals(c.getParent())).findFirst().orElse(null);
                    if (p != null) {
                        category.setValue(p.getName());
                    }
                    subcategory.setValue(c.getName());
                }
            }
        }
    }

    @FXML
    private void initialize() {
        JDBCClient jdbcClient;
        try {
            jdbcClient = new JDBCClient();
            loadCategories(jdbcClient);
            loadCities(jdbcClient);
            sortedSubcategorMap = new TreeMap<String, String>(cityMap);
            sortedCategorMap = new TreeMap<String, String>(categorMap);
            citiescategory.setItems(FXCollections.observableArrayList(sortedSubcategorMap.keySet()));
            category.setItems(FXCollections.observableArrayList(sortedCategorMap.keySet()));
            category.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                    String parentKey = sortedCategorMap.get(observable.getValue().toString()).toString();
                    ArrayList<String> subcatArray = new ArrayList<String>();
                    try {
                        JDBCClient jdbcClient = new JDBCClient();
                        subcategorMap = new HashMap<String, String>();
                        for (Category item : jdbcClient.categorySelectChild(parentKey)) {
                            subcategorMap.put(item.getName(), item.getUrl());
                        }
                        sortedSubcategorMap = new TreeMap<String, String>(subcategorMap);
                        subcategory.setItems(FXCollections.observableArrayList(sortedSubcategorMap.keySet()));
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
    }

    public void actionSearch(ActionEvent actionEvent) {
        Stage stageClose = (Stage) toSearch.getScene().getWindow();
        if (urlAd.getText().isEmpty()) {
            if (citiescategory.getValue() != null) {
                String cityValue = cityMap.get(citiescategory.getValue().toString());
                String categoryValue = "";
                String subcategoryValue = "";
                if (subcategory.getValue() != null) {
                    subcategoryValue = subcategorMap.get(subcategory.getValue().toString());
                }
                if (category.getValue() != null) {
                    categoryValue = categorMap.get(category.getValue().toString());
                }
                int minPrice = 0;
                try {
                    minPrice = Integer.parseInt(startPrice.getText().isEmpty() ? "0" : startPrice.getText());
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Минимальная цена указана неверно");
                    alert.showAndWait();
                    return;
                }
                int maxPrice = 0;
                try {
                    maxPrice = Integer.parseInt(finishPrice.getText().isEmpty() ? "0" : finishPrice.getText());
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Максимальная цена указана неверно");
                    alert.showAndWait();
                    return;
                }
                if (!startPrice.getText().isEmpty() && !finishPrice.getText().isEmpty() && minPrice > maxPrice) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Минимальная цена не может быть больше максимальной");
                    alert.showAndWait();
                    return;
                }

/*Добавление фильтра в бд*/
                sample.dbclasses.Filter filterBD = new sample.dbclasses.Filter(cityValue, categoryValue.toString(), subcategoryValue.toString(),
                        minPrice, maxPrice, photocheck.isSelected(), "");
                try {
                    JDBCClient jdbcClient = new JDBCClient();
                    jdbcClient.filterDeleteTable();
                    jdbcClient.adDeleteTable();
                    jdbcClient.closeStatement();
                    jdbcClient.closeConnection();
                    jdbcClient = new JDBCClient();
                    jdbcClient.filterAdd(filterBD);
                    jdbcClient.closeStatement();
                    jdbcClient.closeConnection();

                    stageClose.close();
                    app.refreshApp();
                } catch (Exception e) {
                    e.printStackTrace();
                    stageClose.close();
                    app.refreshApp();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Обязательно укажите город");
                alert.showAndWait();
                return;
            }
        } else {
            Filter filter = new Filter(urlAd.getText());
            sample.dbclasses.Filter filterBD = new sample.dbclasses.Filter("", "", "", 0, 0, false, filter.toRawQuery());
            try {
                JDBCClient jdbcClient = new JDBCClient();
                jdbcClient = new JDBCClient();
                jdbcClient.filterDeleteTable();
                jdbcClient.adDeleteTable();
                jdbcClient.closeStatement();
                jdbcClient.closeConnection();
                jdbcClient = new JDBCClient();
                jdbcClient.filterAdd(filterBD);
                jdbcClient.closeStatement();
                jdbcClient.closeConnection();
                stageClose.close();
                app.refreshApp();
            } catch (Exception e) {
                e.printStackTrace();
                stageClose.close();
                app.refreshApp();
            }
        }
    }

    private void loadCategories(JDBCClient jdbcClient) {
        categorMap = new HashMap<String, String>();
        try {
            for (Category item : jdbcClient.categorySelectChild("1")) {
                categorMap.put(item.getName(), item.getUrl());
            }
            sortedCategorMap = new TreeMap<String, String>(categorMap);
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
            sortedCityMap = new TreeMap<String, String>(cityMap);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
