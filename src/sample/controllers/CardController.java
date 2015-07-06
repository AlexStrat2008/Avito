package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
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
    public CheckBox saveAd;
    public TextField urlAd;
    public TextField urlPhoto;
    public TextArea comment;

    public CardController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/view/card.fxml"));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setInfo(String name, String description, Long price, String foto, String url, String comment, boolean saveAd, String phone) {
        this.description.setText(description);
        this.price.setText(price != null ? price.toString() : "--");
        this.foto.setImage(new Image(foto == null || foto.isEmpty() ? "/sample/image/nophoto.jpg" : foto));
        this.name.setText(name);
        this.urlAd.setText(url);
        this.urlPhoto.setText(foto == null ? "" : foto.toString());
        this.phoneAd.setText(phone);
        this.saveAd.setSelected(saveAd);
        this.comment.setText(comment);
    }

    public AnchorPane getItem() {
        return itemPane;
    }

    public void onAcionSaveAd(ActionEvent actionEvent) {
        try {
            JDBCClient jdbcClient = new JDBCClient();
            if (saveAd.isSelected() && jdbcClient.isAdExistsByUrl(urlAd.getText()))
                jdbcClient.adAdd(urlAd.getText(), name.getText(), urlPhoto.getText(), Integer.parseInt(price.getText()), description.getText(), (phoneAd.getText().isEmpty() ? "" : phoneAd.getText()), comment.getText().isEmpty() ? "" : comment.getText());
            else
                jdbcClient.adDeleteByURL(urlAd.getText());
            jdbcClient.closeStatement();
            jdbcClient.closeConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(saveAd.isSelected());
    }
}
