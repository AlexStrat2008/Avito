package sample.models;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * Created by Alexandr on 04.07.2015.
 */
public class Filter {
    private String city;
    private long maxPrice;
    private long minPrice;
    private boolean onlyWithPhoto;
    private String category;

    public Filter(String city, long maxPrice, long minPrice, boolean onlyWithPhoto, String category) {
        this.city = city;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
        this.onlyWithPhoto = onlyWithPhoto;
        this.category = category;
    }

    public String toRawQuery()
    {
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public long getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(long maxPrice) {
        this.maxPrice = maxPrice;
    }

    public long getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(long minPrice) {
        this.minPrice = minPrice;
    }

    public boolean isOnlyWithPhoto() {
        return onlyWithPhoto;
    }

    public void setOnlyWithPhoto(boolean onlyWithPhoto) {
        this.onlyWithPhoto = onlyWithPhoto;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
