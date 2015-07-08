package sample.models;

import sample.api.AvitoAd;

import java.net.URI;
import java.time.LocalDateTime;

/**
 *
 * not enough cool
 */
public class ListAd extends AvitoAd{

    private String comment;
    private String phone;
    private Boolean isFavorit;
    public ListAd(String name, Long price, URI photo, String description, URI uri, LocalDateTime dateTime, String comment, String phone, Boolean isFavorit) {
        super(name, price, photo, description, uri, dateTime);
        this.comment = comment;
        this.phone = phone;
        this.isFavorit =isFavorit;
    }


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean isFavorit() {
        return isFavorit;
    }

    public void setIsFavorit(Boolean isFavorit) {
        this.isFavorit = isFavorit;
    }
}
