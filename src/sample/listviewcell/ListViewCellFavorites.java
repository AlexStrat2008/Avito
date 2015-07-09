package sample.listviewcell;

import javafx.scene.control.ListCell;
import sample.controllers.CardController;
import sample.dbclasses.Ad;

/**
 * Created by strat on 06.07.15.
 */
public class ListViewCellFavorites extends ListCell<Ad> {
    @Override
    protected void updateItem(Ad item, boolean empty) {
        super.updateItem(item, empty);
        if(empty)
        {
            setGraphic(null);
            return;
        }
        if (item != null) {
            CardController cardController = new CardController();
            cardController.setInfo(item.getName(), item.getDescription(), item.getPrice().longValue(), item.getUrl_photo(), item.getUrl(), item.getComment(), item.getPhone(), true);
            try {
                setGraphic(cardController.getItem());
            } catch (Exception e) {
                System.out.println("Сылка немного не правильная, но я всеравно все покажу :))");
            }
        }
    }
}
