package sample.api;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

//import javax.ws.rs.core.*;

//import org.apache.commons.lang3.StringEscapeUtils;

/**
 * Created by Alexandr on 30.06.2015.
 */
public class AvitoApi {

    private final static URI hostURL = URI.create("http://avito.ru/");

    public String test() throws IOException{
        Document doc = Jsoup.connect(hostURL.resolve("ulyanovsk/avtomobili?p=2").toURL().toString()).get();
        String title = doc.title();
        return  title;
    }

    /*
     * �������������
        AvitoApi avitoApi = new AvitoApi();
        try {
            List<AvitoAd> ads = avitoApi.getAdsFromRawQuery("ulyanovsk/avtomobili?p=2");
            for(AvitoAd ad : ads) {
                System.out.println(ad);
            }

            System.out.println(ads.size());
        } catch(IOException e) {
            System.out.println(e.getMessage() != null ? e.getMessage() : "error");
        }
     */
    public List<AvitoAd> getAdsFromRawQuery(String query) throws IOException, URISyntaxException {
        Document doc = Jsoup.connect(hostURL.resolve(query).toURL().toString()).get();

        Elements items = doc.select("div.catalog-list div.item");

        ArrayList<AvitoAd> ads = new ArrayList<AvitoAd>();

        for (Element item : items) {
            URI uri = getURIFromElement(item);
            ads.add(new AvitoAd(
                    getNameFromElement(item),
                    getPriceFromElement(item),
                    getPhotoFromElement(item),
                    getAdDescription(uri),
                    uri));
        }

        return  ads;
    }
    /*
    AvitoApi avitoApi = new AvitoApi();
        try {
            List<AvitoAd> ads = avitoApi.getAds("ulyanovsk", 2, 1000000, 100000, false, "avtomobili");
            for(AvitoAd ad : ads) {
                System.out.println(ad);
            }

            System.out.println(ads.size());
        } catch(IOException e) {
            System.out.println(e.getMessage() != null ? e.getMessage() : "error");
        }
     */
    /*public List<AvitoAd> getAds(String city, int page, long maxPrice, long minPrice, boolean onlyWithPhoto, String... categories) throws IOException, URISyntaxException {
        UriBuilder uriBuilder = UriBuilder
                .fromUri("")
                .segment(city)
                .segment(categories)
                .queryParam("p", page)
                .queryParam("pmax", maxPrice)
                .queryParam("pmin", minPrice)
                .queryParam("i", onlyWithPhoto ? "1" : "0");
        URI uri = uriBuilder.build();

        return getAdsFromRawQuery(uri.toString());
    }*/

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

    private URI getPhotoFromElement(Element element) throws URISyntaxException {
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

    private  String getAdDescription(URI adUri) throws IOException{
        Document doc = Jsoup.connect(hostURL.resolve(adUri).toURL().toString()).get();
        StringBuilder text = new StringBuilder("");
        Elements itemParams = doc.select("div.description-expanded div.item-params");
        itemParams.forEach(element -> {
            text.append(element.text() != null ? element.text() : "");
            text.append(System.lineSeparator());
        });

        Elements descriptions = doc.select("div.description-text > div[itemprop=description]").first().getElementsByTag("p");
        descriptions.forEach(p -> {
            text.append(p.text());
            text.append(System.lineSeparator());
        });
        return text.toString();
    }

    private URI getURIFromElement(Element element) {
        String uri = element.select("div.description > h3.title > a").first().attr("href");
        return URI.create(uri);
    }
}