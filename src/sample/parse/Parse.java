package sample.parse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by NIKMC-I on 02.07.2015.
 */
public class Parse {
    private static String URL = "https://www.avito.ru/map";
    private static String CitiesURL = "https://www.avito.ru/";
    private static String mainUrl = "http://www.avito.ru";

    public static void parseCategories () {
        try {
            Document doc  = Jsoup.connect(URL).get();
            Elements categories = doc.select("dl");
            for (org.jsoup.nodes.Element categor : categories) {
                org.jsoup.nodes.Element title = categor.select("dt").first();
                Elements _categories = categor.select("dd");
                System.out.println(mainUrl + title.select("a").first().attr("href")); // address main categories
                System.out.println("main" + "\t" + title.select("a").first().html()); // Name main categories

                for (org.jsoup.nodes.Element element : _categories) {
                    org.jsoup.nodes.Element links = element.select("a").first();
                    String linkHref = links.attr("href");
                    System.out.println("\t\t"+mainUrl+linkHref); // address
                    String linkInnerH = links.html();
                    System.out.println("\t\t\t"+linkInnerH); // Name other categorie
                }
                System.out.println("sdfsdf");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void parseCities() {
        try {
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
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
