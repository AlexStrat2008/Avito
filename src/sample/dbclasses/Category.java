package sample.dbclasses;

public class Category {
    private int id;
    private String name;
    private String url;
    private String parent;

    public Category() {}

    public Category(String _name) {
        this.name = _name;
    }

    public Category(int _id, String _URL, String _name, String _parent) {
        this.id = _id;
        this.url = _URL;
        this.name = _name;
        this.parent = _parent;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getParent() { return parent; }
    public void setParent(String parent) { this.parent = parent; }
}
