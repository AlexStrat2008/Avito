package sample.dbclasses;

public class Ad {
    private int id;
    private String url;
    private String name;
    private String url_photo;

    private Integer price;
    private String description;
    private String phone;
    private String comment;

    public Ad() {
    }

    public Ad(String _url) {
        this.url = _url;
    }

    public Ad(int _id, String _url, String _name, String _url_photo, Integer _price,
              String _description, String _phone, String _comment) {
        this.id = _id;
        this.url = _url;
        this.name = _name;
        this.url_photo = _url_photo;
        this.price = _price;
        this.description = _description;
        this.phone = _phone;
        this.comment = _comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl_photo() {
        return url_photo;
    }

    public void setUrl_photo(String url_photo) {
        this.url_photo = url_photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
