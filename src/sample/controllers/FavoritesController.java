package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import sample.custom.NumberTextField;
import sample.dbclasses.Ad;
import sample.dbclasses.JDBCClient;
import sample.listviewcell.ListViewCellFavorites;

import java.sql.SQLException;

/**
 * Created by strat on 06.07.15.
 */
public class FavoritesController {

    public ListView listViewFavorites;
    private ObservableList<Ad> observableList = FXCollections.observableArrayList();

    @FXML
    public NumberTextField phoneFilter;

    @FXML
    private void initialize() {
        try {
            JDBCClient jdbcClient = new JDBCClient();
            observableList.addAll(jdbcClient.getAdFavoritAll());
            jdbcClient.closeStatement();
            jdbcClient.closeConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        setListView();
    }

    public void setListView(){
        FilteredList<Ad> filteredData = new FilteredList<Ad>(observableList, x -> true);
        phoneFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(ad -> {
                String filter = newValue;
                if (filter == null || filter.isEmpty()) {
                    return true;
                } else {
                    return ad.getPhone().contains(filter);
                }
            });
        });
        listViewFavorites.setItems(filteredData);
        listViewFavorites.setCellFactory(param -> new ListViewCellFavorites());
    }
}
