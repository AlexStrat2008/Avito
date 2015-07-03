package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import sample.dbclasses.Category;
import sample.dbclasses.JDBCClient;
import sample.parse.Parse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import java.io.IOException;

public class Main extends Application {

    public static JDBCClient jdbcClient;
    public static HashMap<String, String> citys;
    public static HashMap<String, String> categories_;
    public static HashMap<String, String> subcategories_;
    public static ArrayList<Category> categories;

    private static String URL = "https://www.avito.ru/map";
    private static String CitiesURL = "https://www.avito.ru/";

    private static String mainUrl = "http://www.avito.ru";

    static final String DB_URL = "jdbc:postgresql://localhost:5432/avitodb";
    static final String LOGIN = "postgres";
    static final String PASSWORD = "1";

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/sample/view/filter.fxml"));
        primaryStage.setTitle("filter");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    
    public static void main(String[] args) {

        new TrayIcon();
        launch(args);
//        parseCategories();
        try {
            jdbcClient = new JDBCClient();
        } catch (ClassNotFoundException e) {
            System.out.println(e.getException());
        }
        if(jdbcClient.isTable("category"))
            Parse.parseCategories(jdbcClient);
        loadCities();
        loadCategories();
        launch(args);


    }

    private static void loadCategories() {
        categories_ = new HashMap<String, String>();
        subcategories_ = new HashMap<String, String>();
        try {
            for (Category item : jdbcClient.categorySelectParent()) {
                categories_.put(item.getName(), item.getURL());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void loadCities() {
        citys = new HashMap<String, String>();
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
                    citys.put(linkInnerH, linkHref.substring(15));
                }
            }
            citys.put("РџРѕ РІСЃРµР№ Р РѕСЃСЃРёРё", "rossiya");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
