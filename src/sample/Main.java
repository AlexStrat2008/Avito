package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import sample.dbclasses.JDBCClient;
import sample.parse.Parse;

public class Main extends Application {

    public static JDBCClient jdbcClient;

    private static String URL = "https://www.avito.ru/map";
    private static String CitiesURL = "https://www.avito.ru/";

    private static String mainUrl = "http://www.avito.ru";

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/sample/view/filter.fxml"));
        primaryStage.setTitle("filter");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        Parse.parseCities();
        Parse.parseCategories();
        try {
            jdbcClient = new JDBCClient();
        } catch (ClassNotFoundException e) {
            System.out.println(e.getException());
        }
        launch(args);
    }


}
