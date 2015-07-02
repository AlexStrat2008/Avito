package sample.api;

import java.net.URI;

/**
 * Created by Alexandr on 30.06.2015.
 */
public class AvitoAd {

    private String name;
    private Long price;
    private URI photo;
    private String description;
    private URI uri;

    public AvitoAd(String name, Long price, URI photo, String description, URI uri) {

        this.name = name;
        this.price = price;
        this.photo = photo;
        this.description = description;
        this.uri = uri;
    }

    @Override
    public  String toString() {
        return String.format("Name: %s Price: %s Photo: %s Uri: %s Description: %s", getName(), getPrice(), getPhoto(), getURI(), getDescription());
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public URI getPhoto() {
        return photo;
    }

    public String getDescription() {
        return description;
    }

    public URI getURI() {
        return uri;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public void setPhoto(URI photo) {
        this.photo = photo;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }
}

