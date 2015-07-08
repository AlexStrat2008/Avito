package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import sample.dbclasses.Ad;
import sample.dbclasses.JDBCClient;
import sample.listviewcell.ListViewCellFavorites;

import java.sql.SQLException;

/**
 * Created by strat on 06.07.15.
 */
public class FavoritesController {

    public ListView listViewFavorites;
    private ObservableList<Ad> observableList = FXCollections.observableArrayList();;

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
        listViewFavorites.setItems(observableList);
        listViewFavorites.setCellFactory(param -> new ListViewCellFavorites());
    }
}
