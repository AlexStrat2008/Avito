package sample.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * Created by strat on 30.06.15.
 */
public class CardController{
    public AnchorPane itemPane;
    public ImageView foto;
    public Label price;
    public Label description;
    public Label name;

    public CardController(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/view/card.fxml"));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setInfo(String name, String description, Long price, Image foto) {
        this.description.setText(description);
        this.price.setText(price.toString());
        this.foto.setImage(foto);
        this.name.setText(name);
    }

    public AnchorPane getItem()
    {
        return itemPane;
    }

}
