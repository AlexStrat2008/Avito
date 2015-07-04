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
                " 'name' TEXT, 'url_photo' TEXT, 'startPrice' INT, 'finishPrice' INT, 'description' TEXT, 'phone' TEXT, " +
                "'comment' TEXT);");
        System.out.println("Таблица объявления создана!");
    }

    public void filterAdd(String city, String category, String subcategory, Integer startPrice, Integer finishPrice,
                          boolean isPhoto, String filterURL) throws SQLException {
        String query = "INSERT INTO 'filter' ('city', 'category', 'subcategory', 'startPrice', 'finishPrice', "
                + "'isPhoto', 'filterURL') VALUES ('" + city + "', '" + category + "','" + subcategory + "','"
                + startPrice + "','" + finishPrice + "','" + isPhoto + "','" + filterURL + "'); ";
        statement.execute(query);
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

    public void categoryAdd(String name, String url, String parent) throws SQLException {
        String query = "INSERT INTO category (name, url, parent) VALUES ('" + name + "','" + url + "','" + parent + "');";
        statement.execute(query);
    }

    //    }
//
//    public Category categorySelect(int id) throws SQLException {
//        String query = "SELECT id, name, url, parent FROM category WHERE ID = " + id + ";";
//        ResultSet resultSet = doExecQuery(connection, query);
//        Category category = new Category();
//        while (resultSet.next()) {
//            category.setId(resultSet.getInt("id"));
//            category.setName(resultSet.getString("name"));
//            category.setUrl(resultSet.getString("url"));
//            category.setParent(resultSet.getString("parent"));
//        }
//        return category;
//    }
//
//    public boolean isTable(String nameTable) {
//        String query = "select * from " + nameTable;
//        Statement statement = null;
//        try {
//            statement = connection.createStatement();
//            ResultSet resultSet = statement.executeQuery(query);
//            int size =0;
//            if (resultSet != null)
//            {
//                resultSet.beforeFirst();
//                resultSet.last();
//                size = resultSet.getRow();
//            }
//            System.out.println(size);
//            return true;
//        } catch (SQLException e) {
//            return false;
//        }
//    }
//
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
