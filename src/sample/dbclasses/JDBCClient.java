package sample.dbclasses;

import org.junit.Before;

import java.sql.*;
import java.util.ArrayList;

public class JDBCClient {
    private static final String JDBC_DRIVER = "org.sqlite.JDBC";
    private static final String DB_URL = "jdbc:sqlite:avitodb.s3db";

    private Connection connection = null;
    private Statement statement = null;

    public JDBCClient() throws ClassNotFoundException, SQLException {
        Class.forName(JDBC_DRIVER);
        connection = DriverManager.getConnection(DB_URL);
        statement = connection.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS 'category' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'name' TEXT, 'url' TEXT, 'parent' TEXT);");
        statement.execute("CREATE TABLE IF NOT EXISTS 'filter' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'city' TEXT," +
                " 'category' TEXT, 'subcategory' TEXT, 'startPrice' INT, 'finishPrice' INT, 'isPhoto' BOOLEAN, 'filterURL' TEXT);");
        statement.execute("CREATE TABLE IF NOT EXISTS 'city' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' TEXT, 'url' TEXT);");
        statement.execute("CREATE TABLE IF NOT EXISTS 'ad' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'url' TEXT," +
                " 'name' TEXT, 'url_photo' TEXT, 'price' INT, 'description' TEXT, 'phone' TEXT, " +
                "'comment' TEXT, 'favorit' BOOLEAN);");
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }

    public void closeStatement() throws SQLException {
        statement.close();
    }


    public void dropAllTable() throws SQLException {
        this.adDeleteTable();
        this.categoryDeleteTable();
        this.filterDeleteTable();
        this.cityDeleteTable();
    }

    public boolean isCityEmpty() {
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery("SELECT * FROM city LIMIT 1;");
            int a = resultSet.getFetchDirection();
            return false;
        } catch (SQLException e) {
            return true;
        }
    }

    public boolean isFilterEmpty() {
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery("SELECT * FROM filter LIMIT 1;");
            int a = resultSet.getFetchDirection();
            return false;
        } catch (SQLException e) {
            return true;
        }
    }

    public boolean isAdEmpty() {
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery("SELECT * FROM ad LIMIT 1;");
            int a = resultSet.getFetchDirection();
            return false;
        } catch (SQLException e) {
            return true;
        }
    }

    public boolean isCatgoryEmpty() {
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery("SELECT * FROM category LIMIT 1;");
            int a = resultSet.getFetchDirection();
            return false;
        } catch (SQLException e) {
            return true;
        }
    }

    /*
        Фильтр
     */

    @Before
    public void filterAdd(String city, String category, String subcategory, Integer startPrice, Integer finishPrice,
                          boolean isPhoto, String filterURL) throws SQLException {
        String query = "INSERT INTO 'filter' ('city', 'category', 'subcategory', 'startPrice', 'finishPrice', "
                + "'isPhoto', 'filterURL') VALUES ('" + city + "', '" + category + "','" + subcategory + "','"
                + startPrice + "','" + finishPrice + "','" + isPhoto + "','" + filterURL + "'); ";
        statement.execute(query);
    }

    public void filterAdd(Filter filter) throws SQLException {
        String query = "INSERT INTO 'filter' ('city', 'category', 'subcategory', 'startPrice', 'finishPrice', "
                + "'isPhoto', 'filterURL') VALUES ('" + filter.getCity() + "', '" + filter.getCategory() + "','" + filter.getSubcategory() + "','"
                + filter.getStartPrice() + "','" + filter.getFinishPrice() + "','" + filter.getIsPhoto() + "','" + filter.getFilterURL() + "'); ";
        statement.execute(query);
    }

    public void filterDeleteTable() throws SQLException {
        statement.execute("DROP TABLE filter;");
    }

    public Integer getFilterIDByURL(String url) throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT id FROM 'filter' WHERE filterURL = '" + url + "'");
        return resultSet.getInt("id");
    }

    public void filterDelete() throws SQLException {
        statement.execute("DELETE FROM 'filter'");
    }

    public void filterDeleteByURL(String url) throws SQLException {
        statement.execute("DELETE FROM 'filter' WHERE filterURL = '" + url + "'");
    }

    public void filterDeleteByID(int id) throws SQLException {
        statement.execute("DELETE FROM 'filter' WHERE id = '" + id + "'");
    }


    public void filterUpdateByID(int id, String city, String category, String subcategory, int startPrice, int finishPrice,
                                 boolean isPhoto, String filterURL) throws SQLException {
        statement.execute("UPDATE filter SET city = '" + city + "', category = '" + category + "', subcategory = '" + subcategory + "', startPrice = '" + startPrice + "', finishPrice = '" + finishPrice + "', isPhoto = '" + isPhoto + "', filterURL = '" + filterURL + "' where id = '" + id + "';");
    }


    public void filterUpdateByURL(String filterURL, String city, String category, String subcategory, int startPrice, int finishPrice,
                                  boolean isPhoto) throws SQLException {
        statement.execute("UPDATE filter SET city = '" + city + "', category = '" + category + "', subcategory = '" + subcategory + "', startPrice = '" + startPrice + "', finishPrice = '" + finishPrice + "', isPhoto = '" + isPhoto + "' where filterURL = '" + filterURL + "';");
    }

    public ArrayList<Filter> getFilterByURL(String url) throws SQLException {
        ArrayList<Filter> filters;
        ResultSet resultSet = statement.executeQuery("SELECT * FROM 'filter' WHERE filterURL = '" + url + "'");
        if (resultSet != null) {
            filters = new ArrayList<>();
            Filter filter;
            while (resultSet.next()) {
                filter = new Filter();
                filter.setId(resultSet.getInt("id"));
                filter.setCity(resultSet.getString("city"));
                filter.setCategory(resultSet.getString("category"));
                filter.setSubcategory(resultSet.getString("subcategory"));
                filter.setStartPrice(resultSet.getInt("startPrice"));
                filter.setFinishPrice(resultSet.getInt("finishPrice"));
                filter.setIsPhoto(resultSet.getBoolean("isPhoto"));
                filter.setFilterURL(resultSet.getString("filterURL"));
                filters.add(filter);
            }
            return filters;
        } else
            return null;
    }

    public ArrayList<Filter> getFilterByID(int id) throws SQLException {
        ArrayList<Filter> filters;
        ResultSet resultSet = statement.executeQuery("SELECT * FROM 'filter' WHERE id = '" + id + "'");
        if (resultSet != null) {
            filters = new ArrayList<>();
            Filter filter;
            while (resultSet.next()) {
                filter = new Filter();
                filter.setId(resultSet.getInt("id"));
                filter.setCity(resultSet.getString("city"));
                filter.setCategory(resultSet.getString("category"));
                filter.setSubcategory(resultSet.getString("subcategory"));
                filter.setStartPrice(resultSet.getInt("startPrice"));
                filter.setFinishPrice(resultSet.getInt("finishPrice"));
                filter.setIsPhoto(resultSet.getBoolean("isPhoto"));
                filter.setFilterURL(resultSet.getString("filterURL"));
                filters.add(filter);
            }
            return filters;
        } else
            return null;
    }

    /*Изменения Ивана*/
    public Filter GetFilterByID(int id) throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT * FROM 'filter' WHERE id = '" + id + "'");
        Filter filter = new Filter();
        if (resultSet != null) {
            while (resultSet.next()) {
                filter.setId(resultSet.getInt("id"));
                filter.setCity(resultSet.getString("city"));
                filter.setCategory(resultSet.getString("category"));
                filter.setSubcategory(resultSet.getString("subcategory"));
                filter.setStartPrice(resultSet.getInt("startPrice"));
                filter.setFinishPrice(resultSet.getInt("finishPrice"));
                filter.setIsPhoto(resultSet.getBoolean("isPhoto"));
                filter.setFilterURL(resultSet.getString("filterURL"));
            }
            return filter;
        } else
            return filter;
    }

    public ArrayList<Filter> getFilter() throws SQLException {
        ArrayList<Filter> filters;
        ResultSet resultSet = statement.executeQuery("SELECT * FROM 'filter'");
        if (resultSet != null) {
            filters = new ArrayList<>();
            Filter filter;
            while (resultSet.next()) {
                filter = new Filter();
                filter.setId(resultSet.getInt("id"));
                filter.setCity(resultSet.getString("city"));
                filter.setCategory(resultSet.getString("category"));
                filter.setSubcategory(resultSet.getString("subcategory"));
                filter.setStartPrice(resultSet.getInt("startPrice"));
                filter.setFinishPrice(resultSet.getInt("finishPrice"));
                filter.setIsPhoto(resultSet.getBoolean("isPhoto"));
                filter.setFilterURL(resultSet.getString("filterURL"));
                filters.add(filter);
            }
            return filters;
        } else
            return null;
    }


    /*
        Города
     */
    public void cityAdd(String name, String url) throws SQLException {
        statement.execute("INSERT INTO 'city' (name, url) VALUES ('" + name + "','" + url + "');");
    }

    public void cityDeleteTable() throws SQLException {
        statement.execute("DROP TABLE city;");
    }

    public Integer getCityIDByURL(String url) throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT id FROM 'city' WHERE url = '" + url + "'");
        return resultSet.getInt("id");
    }

    public void cityDeleteByURL(String url) throws SQLException {
        statement.execute("DELETE FROM 'city' WHERE url = '" + url + "'");
    }

    public void cityDeleteByID(int id) throws SQLException {
        statement.execute("DELETE FROM 'city' WHERE id = '" + id + "'");
    }


    public void cityUpdateByID(int id, String name, String url) throws SQLException {
        statement.execute("UPDATE city SET name = '" + name + "', url = '" + url + "' where id = '" + id + "';");
    }


    public void cityUpdateByURL(String url, String name) throws SQLException {
        statement.execute("UPDATE city SET name = '" + name + "' where url = '" + url + "';");
    }

    public ArrayList<City> getCityByURL(String url) throws SQLException {
        ArrayList<City> cities;
        ResultSet resultSet = statement.executeQuery("SELECT * FROM 'city' WHERE url = '" + url + "';");
        if (resultSet != null) {
            cities = new ArrayList<>();
            City city;
            while (resultSet.next()) {
                city = new City();
                city.setId(resultSet.getInt("id"));
                city.setName(resultSet.getString("name"));
                city.setURL(resultSet.getString("url"));
                cities.add(city);
            }
            return cities;
        } else
            return null;
    }

    public ArrayList<City> getCityAll() throws SQLException {
        ArrayList<City> cities;
        ResultSet resultSet = statement.executeQuery("SELECT * FROM 'city'");
        if (resultSet != null) {
            cities = new ArrayList<>();
            City city;
            while (resultSet.next()) {
                city = new City();
                city.setId(resultSet.getInt("id"));
                city.setName(resultSet.getString("name"));
                city.setURL(resultSet.getString("url"));
                cities.add(city);
            }
            return cities;
        } else
            return null;
    }

    public ArrayList<City> getCityByID(int id) throws SQLException {
        ArrayList<City> cities;
        ResultSet resultSet = statement.executeQuery("SELECT * FROM 'city' WHERE id = '" + id + "';");
        if (resultSet != null) {
            cities = new ArrayList<>();
            City city;
            while (resultSet.next()) {
                city = new City();
                city.setId(resultSet.getInt("id"));
                city.setName(resultSet.getString("name"));
                city.setURL(resultSet.getString("url"));
                cities.add(city);
            }
            return cities;
        } else
            return null;
    }

    /*
        Категории
     */
    public void categoryAdd(String name, String url, String parent) throws SQLException {
        String query = "INSERT INTO category (name, url, parent) VALUES ('" + name + "','" + url + "','" + parent + "');";
        statement.execute(query);
    }

    public void categoryDeleteTable() throws SQLException {
        statement.execute("DROP TABLE category;");
    }

    public Integer getCategoryIDByURL(String url) throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT id FROM 'category' WHERE url = '" + url + "'");
        return resultSet.getInt("id");
    }

    public void categoryDeleteByURL(String url) throws SQLException {
        statement.execute("DELETE FROM 'category' WHERE url = '" + url + "'");
    }

    public void categoryDeleteByID(int id) throws SQLException {
        statement.execute("DELETE FROM 'category' WHERE id = '" + id + "'");
    }


    public void categoryUpdateByID(int id, String name, String url, String parent) throws SQLException {
        statement.execute("UPDATE category SET name = '" + name + "', url = '" + url + "', parent = '" + parent + "' where id = '" + id + "';");
    }


    public void categoryUpdateByURL(String url, String name, String parent) throws SQLException {
        statement.execute("UPDATE category SET name = '" + name + "', parent = '" + parent + "' where url = '" + url + "';");
    }

    public ArrayList<Category> getCategoryByURL(String url) throws SQLException {
        ArrayList<Category> categories;
        ResultSet resultSet = statement.executeQuery("SELECT * FROM 'category' WHERE url = '" + url + "'");
        if (resultSet != null) {
            categories = new ArrayList<>();
            Category category;
            while (resultSet.next()) {
                category = new Category();
                category.setId(resultSet.getInt("id"));
                category.setName(resultSet.getString("name"));
                category.setParent(resultSet.getString("parent"));
                category.setUrl(resultSet.getString("url"));
                categories.add(category);
            }
            return categories;
        } else
            return null;
    }

    public ArrayList<Category> getCategoryByID(int id) throws SQLException {
        ArrayList<Category> categories;
        ResultSet resultSet = statement.executeQuery("SELECT * FROM 'category' WHERE id = '" + id + "'");
        if (resultSet != null) {
            categories = new ArrayList<>();
            Category category;
            while (resultSet.next()) {
                category = new Category();
                category.setId(resultSet.getInt("id"));
                category.setName(resultSet.getString("name"));
                category.setParent(resultSet.getString("parent"));
                category.setUrl(resultSet.getString("url"));
                categories.add(category);
            }
            return categories;
        } else
            return null;
    }

    public ArrayList<Category> getCategoryByName(String name) throws SQLException {
        ArrayList<Category> categories;
        ResultSet resultSet = statement.executeQuery("SELECT * FROM 'category' WHERE name = '" + name + "'");
        if (resultSet != null) {
            categories = new ArrayList<>();
            Category category;
            while (resultSet.next()) {
                category = new Category();
                category.setId(resultSet.getInt("id"));
                category.setName(resultSet.getString("name"));
                category.setParent(resultSet.getString("parent"));
                category.setUrl(resultSet.getString("url"));
                categories.add(category);
            }
            return categories;
        } else
            return null;
    }

    public ArrayList<Category> getCategoryAll() throws SQLException {
        ArrayList<Category> categories;
        ResultSet resultSet = statement.executeQuery("SELECT * FROM 'category'");
        if (resultSet != null) {
            categories = new ArrayList<>();
            Category category;
            while (resultSet.next()) {
                category = new Category();
                category.setId(resultSet.getInt("id"));
                category.setName(resultSet.getString("name"));
                category.setParent(resultSet.getString("parent"));
                category.setUrl(resultSet.getString("url"));
                categories.add(category);
            }
            return categories;
        } else
            return null;
    }

    /*
        Объявления
     */
    public void adAdd(String url, String name, String url_photo, int price, String description, String phone, String comment, boolean isFavorit) throws SQLException {
        String query = "INSERT INTO ad (url, name, url_photo, price, description, phone, comment, favorit) VALUES ('" + url + "','" + name + "','" + url_photo + "','" + price + "','" + description + "','" + phone + "','" + comment + "', '" + isFavorit + "');";
        statement.execute(query);
    }

    public void adDeleteTable() throws SQLException {
        statement.execute("DROP TABLE filter;");
    }

    public Integer getAdIDByURL(String url) throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT id FROM 'ad' WHERE url = '" + url + "'");
        return resultSet.getInt("id");
    }

    public void adDeleteByURL(String url) throws SQLException {
        statement.execute("DELETE FROM 'ad' WHERE url = '" + url + "'");
    }

    public void adyDeleteByID(int id) throws SQLException {
        statement.execute("DELETE FROM 'ad' WHERE id = '" + id + "'");
    }


    public void adUpdateByID(int id, String url, String name, String url_photo, int price, String description, String phone, String comment, boolean isFavorit) throws SQLException {
        statement.execute("UPDATE ad SET url = '" + url + "', name = '" + name + "', url_photo = '" + url_photo + "', price = '" + price + "', description = '" + description + "', phone = '" + phone + "', comment = '" + comment + "', favorit = '" + isFavorit + "' where id = '" + id + "';");
    }


    public void adUpdateByURL(String url, String name, String url_photo, int price, String description, String phone, String comment, boolean isFavorit) throws SQLException {
        statement.execute("UPDATE ad SET name = '" + name + "', url_photo = '" + url_photo + "', price = '" + price + "', description = '" + description + "', phone = '" + phone + "', comment = '" + comment + "', favorit = '" + isFavorit + "' where url = '" + url + "';");
    }

    public ArrayList<Ad> getAdByURL(String url) throws SQLException {
        ArrayList<Ad> ads;
        ResultSet resultSet = statement.executeQuery("SELECT * FROM 'ad' WHERE url = '" + url + "'");
        if (resultSet != null) {
            ads = new ArrayList<>();
            Ad ad = new Ad();
            while (resultSet.next()) {
                ad.setId(resultSet.getInt("id"));
                ad.setName(resultSet.getString("name"));
                ad.setUrl(resultSet.getString("url"));
                ad.setUrl_photo(resultSet.getString("url_photo"));
                ad.setPrice(resultSet.getInt("price"));
                ad.setDescription(resultSet.getString("description"));
                ad.setPhone(resultSet.getString("phone"));
                ad.setComment(resultSet.getString("comment"));
                ad.setIsFavorit(resultSet.getBoolean("favorit"));
                ads.add(ad);
            }
            return ads;
        } else
            return null;
    }

    public ArrayList<Ad> getAdByID(int id) throws SQLException {
        ArrayList<Ad> ads;
        ResultSet resultSet = statement.executeQuery("SELECT * FROM 'ad' WHERE id = '" + id + "'");
        if (resultSet != null) {
            ads = new ArrayList<>();
            Ad ad = new Ad();
            while (resultSet.next()) {
                ad.setId(resultSet.getInt("id"));
                ad.setUrl(resultSet.getString("url"));
                ad.setName(resultSet.getString("name"));
                ad.setUrl_photo(resultSet.getString("url_photo"));
                ad.setPrice(resultSet.getInt("price"));
                ad.setDescription(resultSet.getString("description"));
                ad.setPhone(resultSet.getString("phone"));
                ad.setComment(resultSet.getString("comment"));
                ad.setIsFavorit(resultSet.getBoolean("favorit"));
                ads.add(ad);
            }
            return ads;
        } else
            return null;
    }

    public boolean isAdExistsByUrl(String url) {
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery("SELECT id FROM ad WHERE url = '" + url + "';");
            int a = resultSet.getFetchDirection();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public ArrayList<Ad> getAdAll() throws SQLException {
        ArrayList<Ad> ads;
        ResultSet resultSet = statement.executeQuery("SELECT * FROM 'ad'");
        if (resultSet != null) {
            ads = new ArrayList<>();
            Ad ad;
            while (resultSet.next()) {
                ad = new Ad();
                ad.setId(resultSet.getInt("id"));
                ad.setName(resultSet.getString("name"));
                ad.setUrl_photo(resultSet.getString("url_photo"));
                ad.setPrice(resultSet.getInt("price"));
                ad.setDescription(resultSet.getString("description"));
                ad.setPhone(resultSet.getString("phone"));
                ad.setComment(resultSet.getString("comment"));
                ad.setUrl(resultSet.getString("url"));
                ad.setIsFavorit(resultSet.getBoolean("favorit"));
                ads.add(ad);
            }
            return ads;
        } else
            return null;
    }

    public ArrayList<Category> categorySelectChild(String parent) throws SQLException {
        ArrayList<Category> categories;
        ResultSet resultSet = statement.executeQuery("SELECT * FROM 'category' WHERE parent = '" + parent + "'");
        if (resultSet != null) {
            categories = new ArrayList<>();
            Category category;
            while (resultSet.next()) {
                category = new Category();
                category.setId(resultSet.getInt("id"));
                category.setName(resultSet.getString("name"));
                category.setParent(resultSet.getString("parent"));
                category.setUrl(resultSet.getString("url"));
                categories.add(category);
            }
            return categories;
        } else
            return null;
    }

}
