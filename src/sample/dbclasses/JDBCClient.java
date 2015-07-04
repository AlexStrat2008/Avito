package sample.dbclasses;

import java.sql.*;
import java.util.ArrayList;

public class JDBCClient {
    private static final String JDBC_DRIVER = "org.sqlite.JDBC";
    private static final String DB_URL = "jdbc:sqlite:avitodb.s3db";

    private Connection connection = null;
    private Statement statement = null;

    public JDBCClient() throws ClassNotFoundException, SQLException {
        Class.forName(JDBC_DRIVER);
        System.out.println("Драйвер подключен");
        connection = DriverManager.getConnection(DB_URL);
        System.out.println("База Подключена!");
        statement = connection.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS 'category' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'name' TEXT, 'url' TEXT, 'parent' TEXT);");
        System.out.println("Таблица категории создана!");
        statement.execute("CREATE TABLE IF NOT EXISTS 'filter' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'city' TEXT," +
                " 'category' TEXT, 'subcategory' TEXT, 'startPrice' INT, 'finishPrice' INT, 'isPhoto' BOOLEAN, 'filterURL' TEXT);");
        System.out.println("Таблица фильтры создана!");
        statement.execute("CREATE TABLE IF NOT EXISTS 'city' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' TEXT, 'url' TEXT);");
        System.out.println("Таблица города создана!");
        statement.execute("CREATE TABLE IF NOT EXISTS 'ad' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'url' TEXT," +
                " 'name' TEXT, 'url_photo' TEXT, 'price' INT, 'description' TEXT, 'phone' TEXT, " +
                "'comment' TEXT);");
        System.out.println("Таблица объявления создана!");
    }

    public void dropAllTable() throws SQLException {
        this.adDeleteTable();
        this.categoryDeleteTable();
        this.filterDeleteTable();
        this.cityDeleteTable();
    }
    public boolean isCityEmpty(){
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery("SELECT * FROM city LIMIT 1;");
            int a = resultSet.getFetchDirection();
            return false;
        } catch (SQLException e) {
            return true;
        }
    }

    public boolean isFilterEmpty(){
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery("SELECT * FROM filter LIMIT 1;");
            int a = resultSet.getFetchDirection();
            return false;
        } catch (SQLException e) {
            return true;
        }
    }

    public boolean isAdEmpty(){
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery("SELECT * FROM ad LIMIT 1;");
            int a = resultSet.getFetchDirection();
            return false;
        } catch (SQLException e) {
            return true;
        }
    }

    public boolean isCatgoryEmpty(){
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
    public void filterAdd(String city, String category, String subcategory, Integer startPrice, Integer finishPrice,
                          boolean isPhoto, String filterURL) throws SQLException {
        String query = "INSERT INTO 'filter' ('city', 'category', 'subcategory', 'startPrice', 'finishPrice', "
                + "'isPhoto', 'filterURL') VALUES ('" + city + "', '" + category + "','" + subcategory + "','"
                + startPrice + "','" + finishPrice + "','" + isPhoto + "','" + filterURL + "'); ";
        statement.execute(query);
    }

    public void filterDeleteTable() throws SQLException {
        statement.execute("DROP TABLE filter;");
    }

    public Integer getFilterIDByURL(String url) throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT id FROM 'filter' WHERE filterURL = '" + url + "'");
        return resultSet.getInt("id");
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
    /*
        Объявления
     */
    public void adAdd(String url, String name, String url_photo, int price, String description, String phone, String comment) throws SQLException {
        String query = "INSERT INTO ad (url, name, url_photo, price, description, phone, comment) VALUES ('" + url + "','" + name + "','" + url_photo + "','" + price + "','" + description + "','" + phone + "','" + comment + "');";
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


    public void adUpdateByID(int id, String url, String name, String url_photo, int price, String description, String phone, String comment) throws SQLException {
        statement.execute("UPDATE ad SET url = '" + url + "', name = '" + name + "', url_photo = '" + url_photo + "', price = '" + price + "', description = '" + description + "', phone = '" + phone + "', comment = '" + comment + "' where id = '" + id + "';");
    }


    public void adUpdateByURL(String url, String name, String url_photo, int price, String description, String phone, String comment) throws SQLException {
        statement.execute("UPDATE ad SET name = '" + name + "', url_photo = '" + url_photo + "', price = '" + price + "', description = '" + description + "', phone = '" + phone + "', comment = '" + comment + "' where url = '" + url + "';");
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
                ad.setUrl_photo(resultSet.getString("url_photo"));
                ad.setPrice(resultSet.getInt("price"));
                ad.setDescription(resultSet.getString("description"));
                ad.setPhone(resultSet.getString("phone"));
                ad.setComment(resultSet.getString("comment"));
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
                ad.setName(resultSet.getString("name"));
                ad.setUrl_photo(resultSet.getString("url_photo"));
                ad.setPrice(resultSet.getInt("price"));
                ad.setDescription(resultSet.getString("description"));
                ad.setPhone(resultSet.getString("phone"));
                ad.setComment(resultSet.getString("comment"));
            }
            return ads;
        } else
            return null;
    }

    public ArrayList<Category> categorySelectChild(String parent) throws SQLException {
        ArrayList<Category> arrayList = new ArrayList<>();
//        String query = "SELECT id, name, url, parent FROM category WHERE parent = '" + parent + "'";
//        Statement statement = connection.createStatement();
//        ResultSet resultSet = statement.executeQuery(query);
//        while (resultSet.next()) {
//            Category category = new Category();
//            category.setId(resultSet.getInt("id"));
//            category.setName(resultSet.getString("name"));
//            category.setUrl(resultSet.getString("url"));
//            category.setParent(resultSet.getString("parent"));
//            arrayList.add(category);
//        }
        return arrayList;
    }

    //
    public ArrayList<Category> categorySelectParent() throws SQLException {
        ArrayList<Category> arrayList = new ArrayList<>();
//        String query = "SELECT id, name, url, parent FROM category WHERE parent = '1'";
//        Statement statement = connection.createStatement();
//        ResultSet resultSet = statement.executeQuery(query);
//        while (resultSet.next()) {
//            Category category = new Category();
//            category.setId(resultSet.getInt("id"));
//            category.setName(resultSet.getString("name"));
//            category.setUrl(resultSet.getString("url"));
//            category.setParent(resultSet.getString("parent"));
//            arrayList.add(category);
//        }
        return arrayList;
    }


}
