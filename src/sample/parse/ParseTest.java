package sample.parse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by tanyastarshova on 07.07.2015.
 */
public class ParseTest {

    @Test
    public void testParseCategories() throws Exception {
        String URL = "https://www.avito.ru/map";
        Document doc = Jsoup.connect(URL).get();

    }

    @Test
    public void testParseCities() throws Exception {

    }
}