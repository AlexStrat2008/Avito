package sample.models;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 *
 * not enough cool
 */
public class Filter {
    private String city;
    private long maxPrice;
    private long minPrice;
    private boolean onlyWithPhoto;
    private String category;
    private String rawQuery;

    public String getCity() {
        return city;
    }

    public long getMaxPrice() {
        return maxPrice;
    }

    public long getMinPrice() {
        return minPrice;
    }

    public boolean isOnlyWithPhoto() {
        return onlyWithPhoto;
    }

    public String getCategory() {
        return category;
    }

    public Filter(String city, long maxPrice, long minPrice, boolean onlyWithPhoto, String category) {
        this.city = city;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
        this.onlyWithPhoto = onlyWithPhoto;
        this.category = category;
    }

    public Filter(String rawQuery) {
        this.rawQuery = rawQuery==null ? "" : rawQuery;
    }

    public String toRawQuery()
    {
        if (rawQuery != null) return rawQuery;

        UriBuilder uriBuilder = UriBuilder
                .fromUri("")
                .segment(city)
                .segment(category)
                .queryParam("pmax", maxPrice)
                .queryParam("pmin", minPrice)
                .queryParam("i", onlyWithPhoto ? "1" : "0");
        URI uri = uriBuilder.build();

        return uri.toString();
    }


}
