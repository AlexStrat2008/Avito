package sample.parse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import sample.dbclasses.JDBCClient;

import java.io.IOException;
import java.sql.SQLException;


/**
 * Created by NIKMC-I on 02.07.2015.
 */
public class Parse {
    private static String URL = "https://www.avito.ru/map";
    private static String CitiesURL = "https://www.avito.ru/";
    private static String mainUrl = "http://www.avito.ru";
    static final String DB_URL = "jdbc:postgresql://localhost:5432/avitodb";
    static final String LOGIN = "postgres";
    static final String PASSWORD = "alex20";

    public static void parseCategories (JDBCClient client) {

        try {
            Document doc = Jsoup.connect(URL).get();
            Elements categories = doc.select("dl");
            for (org.jsoup.nodes.Element categor : categories) {
                org.jsoup.nodes.Element title = categor.select("dt").first();
                Elements _categories = categor.select("dd");
                String parentUrl = title.select("a").first().attr("href");
                try {
//                    System.out.println(title.select("a").first().html());

                    client.categoryAdd(title.select("a").first().html(), title.select("a").first().attr("href").substring(9),"1");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                for (org.jsoup.nodes.Element element : _categories) {
                    org.jsoup.nodes.Element links = element.select("a").first();
                    String linkHref = links.attr("href");
                    String linkInnerH = links.html();
                    try {
                        if(!linkInnerH.isEmpty()){
                            client.categoryAdd(linkInnerH,linkHref.substring(9),parentUrl.substring(9));
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void parseCities() {
        try {
            JDBCClient jdbcClient = new JDBCClient();
            Document doc  = Jsoup.connect(CitiesURL).get();
            Elements cities = doc.select("div.col-2");
            //System.out.println("\t\t" + cities);
            //Elements _cities = cities.select("cities");

            for (org.jsoup.nodes.Element city : cities) {
                Elements city_ = city.select("*");
                for (org.jsoup.nodes.Element _city : city_) {
                    org.jsoup.nodes.Element links = _city.select("a").first();
                    String linkHref = links.attr("href");
                    System.out.println("\t\t" + linkHref); // address
                    String linkInnerH = links.html();
                    System.out.println("\t\t\t" + linkInnerH); // Name other cities
                    try {
                        jdbcClient.cityAdd(linkInnerH,linkHref,"");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}
