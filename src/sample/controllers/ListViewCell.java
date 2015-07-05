package sample.controllers;

import javafx.scene.control.ListCell;
import sample.api.AvitoAd;

/**
 * Created by strat on 30.06.15.
 */

public class ListViewCell extends ListCell<AvitoAd> {
    @Override
    protected void updateItem(AvitoAd item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            CardController cardController = new CardController();
            cardController.setInfo(item.getName(), item.getDescription(), item.getPrice(), item.getPhoto(), item.getURI().toString());
            try {
                setGraphic(cardController.getItem());
            } catch (Exception e) {
                System.out.println("Сылка немного не правильная, но я всеравно все покажу :))");
            }
        }
    }
}
