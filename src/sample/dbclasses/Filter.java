package sample.dbclasses;

public class Filter {
    private int id;
    private String name;
    private Double priceFirst;
    private Double priceSecond;
    private String city;
    private String category;
    private String subcategory;
    private boolean picture;

    public Filter() {}

    public Filter(String _name) {
        this.name = _name;
    }

    public Filter(int _id, String _name, Double _priceFirst, Double _priceSecond, String _city,
                  String _category, String _subcategory, boolean _picture) {
        this.id = _id;
        this.name = _name;
        this.priceFirst = _priceFirst;
        this.priceSecond = _priceSecond;
        this.city = _city;
        this.category = _category;
        this.subcategory = _subcategory;
        this.picture = _picture;
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getPriceFirst() { return priceFirst; }
    public void setPriceFirst(Double priceFirst) { this.priceFirst = priceFirst; }

    public Double getPriceSecond() { return priceSecond; }
    public void setPriceSecond(Double priceSecond) { this.priceSecond = priceSecond; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getSubcategory() { return subcategory; }
    public void setSubcategory(String subcategory) { this.subcategory = subcategory; }

    public Boolean getPicture() { return picture; }
    public void setPicture(Boolean picture) { this.picture = picture; }
}

