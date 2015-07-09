package sample.listviewcell;

import javafx.scene.control.ListCell;
import sample.api.AvitoAd;
import sample.controllers.CardController;

/**
 * Created by strat on 30.06.15.
 */

public class ListViewCell extends ListCell<AvitoAd> {
    @Override
    protected void updateItem(AvitoAd item, boolean empty) {
        super.updateItem(item, empty);
        if(empty)
        {
            setGraphic(null);
            return;
        }
        if (item != null) {
            CardController cardController = new CardController();
            cardController.setInfo(
                    item.getName(),
                    item.getDescription(),
                    item.getPrice(),
                    item.getPhoto() != null ? item.getPhoto().toString() : "",
                    item.getURI().toString(),
                    "", "", false);
            try {
                setGraphic(cardController.getItem());
            } catch (Exception e) {
                System.out.println("Сылка немного не правильная, но я всеравно все покажу :))");
            }
        }
    }
}
