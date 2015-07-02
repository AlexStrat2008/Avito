package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import sample.dbclasses.JDBCClient;

import java.io.IOException;
import java.util.HashMap;

public class Main extends Application {

    public static JDBCClient jdbcClient;
    public static HashMap<String,String> citys;

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
//        parseCategories();
        loadCities();
        try {
            jdbcClient = new JDBCClient();
        } catch (ClassNotFoundException e) {
            System.out.println(e.getException());
        }
        launch(args);
    }

    private static void parseCategories() {
        try {
            Document doc = Jsoup.connect(URL).get();
            Elements categories = doc.select("dl");
            for (org.jsoup.nodes.Element categor : categories) {
                org.jsoup.nodes.Element title = categor.select("dt").first();
                Elements _categories = categor.select("dd");
                System.out.println(mainUrl + title.select("a").first().attr("href")); // address main categories
                System.out.println("main" + "\t" + title.select("a").first().html()); // Name main categories

                for (org.jsoup.nodes.Element element : _categories) {
                    org.jsoup.nodes.Element links = element.select("a").first();
                    String linkHref = links.attr("href");
                    System.out.println("\t\t" + mainUrl + linkHref); // address
                    String linkInnerH = links.html();
                    System.out.println("\t\t\t" + linkInnerH); // Name other categorie
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadCities() {
        citys = new HashMap<String,String>();
        try {
            Document doc = Jsoup.connect(CitiesURL).get();
            Elements cities = doc.select("div.col-2");
            //System.out.println("\t\t" + cities);
            //Elements _cities = cities.select("cities");

            for (org.jsoup.nodes.Element city : cities) {
                Elements city_ = city.select("*");
                for (org.jsoup.nodes.Element _city : city_) {
                    org.jsoup.nodes.Element links = _city.select("a").first();
                    String linkHref = links.attr("href");
                    String linkInnerH = links.html();
                    citys.put(linkInnerH,linkHref.substring(15));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
