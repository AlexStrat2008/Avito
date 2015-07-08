package sample.dbclasses;

public class Filter {
    private int id;
    private String city;
    private String category;
    private String subcategory;
    private Integer startPrice;
    private Integer finishPrice;
    private boolean isPhoto;
    private String filterURL;

    public Filter() {}

    public Filter(String _filterURL) {
        this.filterURL = _filterURL;
    }

    public Filter(int _id, String _city, String _category, String _subcategory , Integer _startPrice,
                  Integer _finishPrice, boolean _isPhoto, String _filterURL) {
        this.id = _id;
        this.city = _city;
        this.category = _category;
        this.subcategory = _subcategory;
        this.startPrice = _startPrice;
        this.finishPrice = _finishPrice;
        this.isPhoto = _isPhoto;
        this.filterURL = _filterURL;
    }
    public Filter(String _city, String _category, String _subcategory , Integer _startPrice,
                  Integer _finishPrice, boolean _isPhoto, String _filterURL) {
        this.city = _city;
        this.category = _category;
        this.subcategory = _subcategory;
        this.startPrice = _startPrice;
        this.finishPrice = _finishPrice;
        this.isPhoto = _isPhoto;
        this.filterURL = _filterURL;
    }
    public Filter(Filter filter) {
        this.id = filter.getId();
        this.city = filter.getCity(); System.out.println(city);
        this.category = filter.getCategory();
        this.subcategory = filter.getSubcategory();
        this.startPrice = filter.getStartPrice();
        this.finishPrice = filter.getFinishPrice();
        this.isPhoto = filter.getIsPhoto();
        this.filterURL = filter.getFilterURL();
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getSubcategory() { return subcategory; }
    public void setSubcategory(String subcategory) { this.subcategory = subcategory; }

    public Integer getStartPrice() { return startPrice; }
    public void setStartPrice(Integer startPrice) { this.startPrice = startPrice; }

    public Integer getFinishPrice() { return finishPrice; }
    public void setFinishPrice(Integer finishPrice) { this.finishPrice = finishPrice; }

    public Boolean getIsPhoto() { return isPhoto; }
    public void setIsPhoto(Boolean isPhoto) { this.isPhoto = isPhoto; }

    public String getFilterURL() { return filterURL; }
    public void setFilterURL(String filterURL) { this.filterURL = filterURL; }
}

