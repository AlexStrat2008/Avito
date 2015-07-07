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
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.sqlite.JDBC;
import sample.*;
import sample.custom.NumberTextField;
import sample.dbclasses.Category;
import sample.dbclasses.City;
import sample.dbclasses.JDBCClient;
import sample.models.Filter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
    //    map городов
    private HashMap<String, String> cityMap;
    private HashMap<String, String> categorMap;
    private HashMap<String, String> subcategorMap = new HashMap<String, String>();

    private App app;

    public void setApp(App app) {
        this.app = app;
        setView();
    }

    private void setView() {
        Filter filter = app.getFilter();

        //я хотел как лучше (
        ArrayList<Category> allCategories = new ArrayList<>();
        try {
            JDBCClient client = new JDBCClient();
            allCategories = client.getCategoryAll();
            client.closeConnection();
        } catch (Exception e) {

        }

        if (filter != null) {
            urlAd.setText(filter.toRawQuery());
            //need parsing
            finishPrice.setText(filter.getMaxPrice() > 0 ? filter.getMaxPrice() + "" : "");
            startPrice.setText(filter.getMinPrice() > 0 ? filter.getMinPrice() + "" : "");
            photocheck.setSelected(filter.isOnlyWithPhoto());

            String city_value = filter.getCity() != null? filter.getCity() : "";

            for (Map.Entry<String, String> c : cityMap.entrySet()) {
                if (c.getValue().equals(city_value)) {
                    citiescategory.setValue(c.getKey());
                }
            }
//Я не смог исправить все это говно, поэтому добавил еще больше говна
            String category_value = filter.getCategory() != null? filter.getCategory() : "";

                Category c = allCategories.stream().filter(x -> x.getUrl().equals(category_value)).findFirst().orElse(null);
                System.out.println(c);
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

        } else {

        }

    }

    @FXML
    private void initialize() {

        //VANYA I LESHA,  BLYAT, CHE ZA GOVNO?! KAK TAKOE MOZHNO BYLO NAPISAT?!!

        JDBCClient jdbcClient;
        categorMap = new HashMap<String, String>();
        try {
            jdbcClient = new JDBCClient();

            loadCategories(jdbcClient);
            loadCities(jdbcClient);
            sample.dbclasses.Filter editFilter;
            if (!jdbcClient.isFilterEmpty()) {
                System.out.println("filter is not empty" + jdbcClient.getFilter().size());
                editFilter = new sample.dbclasses.Filter(jdbcClient.getFilterByID(1));
                System.out.println("filter = " + editFilter.getCity() + editFilter.getCategory() + editFilter.getSubcategory());

                citiescategory.setItems(FXCollections.observableArrayList(editFilter.getCity()));

                //citiescategory.setValue(editFilter.getCity());

                //category.setValue(editFilter.getCategory());
                category.setItems(FXCollections.observableArrayList(categorMap.keySet()));

                //subcategory.setValue(editFilter.getSubcategory());

                subcategorMap = new HashMap<String, String>();
                //System.out.println(jdbcClient.getCategoryByName(editFilter.getCategory()).get(0).getUrl());
                /*for (Category item : jdbcClient.categorySelectChild(jdbcClient.getCategoryByName(editFilter.getCategory()).get(0).getUrl())) {
                    subcategorMap.put(item.getName(), item.getUrl());
                    System.out.println("subcategory = " + item.getName());

                }*/
                //System.out.println("subcategory = " + subcategorMap.size());

                subcategory.setItems(FXCollections.observableArrayList(subcategorMap.keySet()));




            } else {
                //citiescategory.setItems(FXCollections.observableArrayList(""));
                //category.setItems(FXCollections.observableArrayList(""));
                //subcategory.setItems(FXCollections.observableArrayList(""));
                startPrice.setText(String.valueOf(""));
                finishPrice.setText(String.valueOf(""));
                photocheck.setSelected(false);
                urlAd.setText("");

            }
           // category.setItems(FXCollections.observableArrayList(categorMap.keySet()));
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
        category.setItems(FXCollections.observableArrayList(categorMap.keySet()));
    }

    public void actionSearch(ActionEvent actionEvent) {

        Stage stageClose = (Stage) toSearch.getScene().getWindow();

        if(urlAd.getText().isEmpty()) {
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
                    minPrice = Integer.parseInt(startPrice.getText().isEmpty()? "0" : startPrice.getText());
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Минимальная цена указана неверно");
                    alert.showAndWait();
                    return;
                }
                int maxPrice = 0;
                try {
                    maxPrice = Integer.parseInt(finishPrice.getText().isEmpty()? "0" : finishPrice.getText());
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Максимальная цена указана неверно");
                    alert.showAndWait();
                    return;
                }

/*Добавление фильтра в бд*/
                sample.dbclasses.Filter filterBD = new sample.dbclasses.Filter(cityValue,categoryValue.toString(),subcategoryValue.toString(),
                            minPrice,maxPrice, photocheck.isSelected(), "");
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
/*-----------------------*/
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Обязательно укажите город");
                    alert.showAndWait();
                    return;
                }
        }
        else{

            Filter filter = new Filter(urlAd.getText());
            sample.dbclasses.Filter filterBD = new sample.dbclasses.Filter("","","",
                   0,0, false, filter.toRawQuery());

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
}
