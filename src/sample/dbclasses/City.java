package sample.dbclasses;

public class City {
    private int id;
    private String name;
    private String URL;

    public City() {}

    public City(String _name) {
        this.name = _name;
    }

    public City(int _id, String _URL, String _name) {
        this.id = _id;
        this.URL = _URL;
        this.name = _name;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getURL() { return URL; }
    public void setURL(String URL) { this.URL = URL; }
}
