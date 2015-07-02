package sample.dbclasses;

public class Category {
    private int id;
    private String name;
    private String URL;
    private String parent;

    public Category() {}

    public Category(String _name) {
        this.name = _name;
    }

    public Category(int _id, String _URL, String _name, String _parent) {
        this.id = _id;
        this.URL = _URL;
        this.name = _name;
        this.parent = _parent;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getURL() { return URL; }
    public void setURL(String URL) { this.URL = URL; }

    public String getParent() { return parent; }
    public void setParent(String parent) { this.parent = parent; }
}
