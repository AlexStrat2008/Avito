package sample.controllers;

import javafx.scene.control.Label;
import org.junit.Test;
import static org.junit.Assert.*;

import sample.controllers.CardController;
import javafx.fxml.FXMLLoader;
import sample.dbclasses.JDBCClient;

import static org.junit.Assert.*;

/**
 * Created by tanyastarshova on 07.07.2015.
 */
public class CardControllerTest {
    public Label name;
    private String text;

    @Test
    public void testSetInfo() throws Exception {
        //String name = this.name.setText("");

        System.out.println(name);
    }

    @Test
    public void testOnAcionSaveAd() throws Exception {
        JDBCClient jdbc = new JDBCClient();
    }

    @Test
    public void testSetInfo1() throws Exception {


    }
}