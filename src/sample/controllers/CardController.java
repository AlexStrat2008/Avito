package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import sample.App;
import sample.dbclasses.JDBCClient;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by strat on 30.06.15.
 */
public class CardController {
    public AnchorPane itemPane;
    public ImageView foto;
    public Label price;
    public Label description;
    public Label name;
    public TextField phoneAd;
    public TextField urlAd;
    public TextField urlPhoto;
    public TextArea comment;
    public Button adMore;
    public Button favoritBottom;
    private Image star;

    public CardController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/view/card.fxml"));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        star = new Image(getClass().getResourceAsStream("/sample/image/star.png"));
        favoritBottom.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/sample/image/star.png"))));
    }

    public void setInfo(String name, String description, Long price, String foto, String url, String comment, String phone, boolean saveAd) {
        this.description.setText(description);
        this.price.setText(price != null ? price.toString() : "--");
        this.foto.setImage(new Image(foto == null || foto.isEmpty() ? "/sample/image/nophoto.jpg" : foto));
        this.name.setText(name);
        this.urlAd.setText(url);
        this.urlPhoto.setText(foto == null ? "" : foto.toString());
        this.phoneAd.setText(phone);
        this.comment.setText(comment);
        if(saveAd)
            favoritBottom.setGraphic(new ImageView(star));
    }

    public AnchorPane getItem() {
        return itemPane;
    }

    public void actionAdMore(ActionEvent actionEvent) {
        App.hostServices.showDocument("https://www.avito.ru" + urlAd.getText());
    }

    public void actionFavoritBottom(ActionEvent actionEvent) {
        try {
            JDBCClient jdbcClient = new JDBCClient();
            if (!jdbcClient.isAdExistsByUrlFavorit(urlAd.getText())) {
                jdbcClient.changeAdFavorit(urlAd.getText(), true, comment.getText(), phoneAd.getText());
            } else {
                jdbcClient.changeAdFavorit(urlAd.getText(), false, comment.getText(), phoneAd.getText());
            }
            jdbcClient.closeStatement();
            jdbcClient.closeConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}