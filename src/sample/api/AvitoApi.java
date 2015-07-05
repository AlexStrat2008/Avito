package sample.api;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import sample.com.benjiweber.yield.*;
import sample.com.benjiweber.yield.Yielderable.*;

import sample.models.Filter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import org.apache.commons.lang3.StringEscapeUtils;

/**
 * Created by Alexandr on 30.06.2015.
 */
public class AvitoApi {

    private final static URI hostURL = URI.create("http://avito.ru/");
    private final static HashMap<String, Integer> months = new HashMap<String, Integer>() {{
        put("января", 1);
        put("февраля", 2);
        put("марта", 3);
        put("апреля", 4);
        put("мая", 5);
        put("июня", 6);
        put("июля", 7);
        put("августа", 8);
        put("сентября", 9);
        put("октября", 10);
        put("ноября", 11);
        put("декабря", 12);
    }};

    public Yielderable<AvitoAd> getAdsFromRawQueryYield(String query) {
        return yield -> {
            try {
                Document doc = Jsoup.connect(hostURL.resolve(query).toString()).get();
                Elements items = doc.select("div.catalog-list div.item");
                for (Element item : items) {
                    URI uri = getURIFromElement(item);
                    AvitoAd ad = new AvitoAd(
                            getNameFromElement(item),
                            getPriceFromElement(item),
                            getPhotoFromElement(item),
                            getAdDescription(uri),
                            uri,
                            getDateTimeFromElement(item));

                    yield.returning(ad);
                }
            } catch (Exception e) {
                yield.breaking();
            }

        };
    }

    public Yielderable<AvitoAd> getAdsYield(Filter filter) {
        return getAdsFromRawQueryYield(filter.toRawQuery());
    }

    public List<AvitoAd> getAdsFromRawQuery(String query) throws IOException {
        Document doc = Jsoup.connect(hostURL.resolve(query).toString()).get();

        Elements items = doc.select("div.catalog-list div.item");

        ArrayList<AvitoAd> ads = new ArrayList<AvitoAd>();

        for (Element item : items) {
            URI uri = getURIFromElement(item);
            ads.add(new AvitoAd(
                    getNameFromElement(item),
                    getPriceFromElement(item),
                    getPhotoFromElement(item),
                    getAdDescription(uri),
                    uri,
                    getDateTimeFromElement(item)));
        }
        return  ads;
    }

    public List<AvitoAd> getAds(Filter filter) throws IOException {
        return getAdsFromRawQuery(filter.toRawQuery());
    }

    private String getNameFromElement(Element element) {
        return element.select("div.description > h3.title > a").first().ownText();
    }

    private Long getPriceFromElement(Element element) {
        String text = element.select("div.description > div.about").first().ownText();
        //text = StringEscapeUtils.unescapeHtml4(text);
        text = text.replaceAll("[^0-9]", "");
        Long price;
        try {
            price = Long.parseLong(text);
        } catch (NumberFormatException e) {
            price = null;
        }

        return  price;
    }

    private URI getPhotoFromElement(Element element){
        Element photo = element.select("div.b-photo > a.photo-wrapper > img").first();
        String src = "";
        if (photo != null) {
            if (photo.hasAttr("data-srcpath")) {
                src = photo.attr("data-srcpath");
            } else if (photo.hasAttr("src")) {
                src = photo.attr("src");
            }
        }
        return src.isEmpty()? null : URI.create("http:" + src);

    }

    private  String getAdDescription(URI adUri){
        StringBuilder text = new StringBuilder("");
        Document doc;
        try {
             doc = Jsoup.connect(hostURL.resolve(adUri).toURL().toString()).get();
        } catch (Exception e) {
            return text.toString();
        }
        try {
            Elements itemParams = doc.select("div.description-expanded div.item-params");
            itemParams.forEach(element -> {
                text.append(element.text() != null ? element.text() : "");
                text.append(System.lineSeparator());
            });
        } catch (NullPointerException e) {

        }

        try {
            Elements descriptions = doc.select("div.description-text > div[itemprop=description]").first().getElementsByTag("p");
            descriptions.forEach(p -> {
                text.append(p.text());
                text.append(System.lineSeparator());
            });
        } catch (NullPointerException e) {

        }

        return text.toString();
    }

    private URI getURIFromElement(Element element) {
        String uri = element.select("div.description > h3.title > a").first().attr("href");
        return URI.create(uri);
    }

    private LocalDateTime getDateTimeFromElement(Element element) {
        String sDateTime = element.select("div.description > div.data > div.date").first().ownText().trim();
        String[] words = sDateTime.split(" ");
        LocalDateTime dateTime;
        if (words.length == 2) {
            LocalDate date;
            if (words[0].equals("Сегодня")) {
                date = LocalDate.now();
            } else if (words[0].equals("Вчера")) {
                date = LocalDate.now().minusDays(1);
            } else {
                date = LocalDate.now().minusDays(500);
            }

            LocalTime time = LocalTime.parse(words[1]);
            dateTime = LocalDateTime.of(date, time);

        } else if (words.length == 3) {
            //just fuck off
            int year = LocalDate.now().getYear();
            Integer month = months.get(words[1]);
            int day = Integer.parseInt(words[0]);
            LocalDate date = LocalDate.of(year, month.intValue(), day);
            LocalTime time = LocalTime.parse(words[2]);

            dateTime = LocalDateTime.of(date, time);
        } else {
            dateTime = LocalDateTime.now().minusDays(600);
        }

        return dateTime;
    }
}