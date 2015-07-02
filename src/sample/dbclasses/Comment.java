package sample.dbclasses;

public class Comment {
    private int id;
    private String URL;
    private String description;

    public Comment() {}

    public Comment(String _URL) {
        this.URL = _URL;
    }

    public Comment(int _id, String _URL, String _description) {
        this.id = _id;
        this.URL = _URL;
        this.description = _description;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getURL() { return URL; }
    public void setURL(String URL) { this.URL = URL; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
