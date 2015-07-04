package sample.dbclasses;

import java.sql.*;
import java.util.ArrayList;

public class JDBCClient {
    private static final String JDBC_DRIVER = "org.sqlite.JDBC";
    private static final String DB_URL = "jdbc:sqlite:avitodb.s3db";

    private String category = "CREATE TABLE IF NOT EXISTS 'category' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "'name' TEXT, 'url' TEXT, 'parent' TEXT);";
    private String filter = "CREATE TABLE IF NOT EXISTS 'filter' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'city' TEXT," +
            " 'category' TEXT, 'subcategory' TEXT, 'startPrice' INT, 'finishPrice' INT, 'isPhoto' BOOLEAN, 'filterURL' TEXT);";

    private String city = "CREATE TABLE IF NOT EXISTS 'city' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' TEXT, 'url' TEXT);";
    private String ad = "CREATE TABLE IF NOT EXISTS 'ad' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'url' TEXT," +
            " 'name' TEXT, 'url_photo' TEXT, 'startPrice' INT, 'finishPrice' INT, 'description' TEXT, 'phone' TEXT, " +
            "'comment' TEXT);";
    private Connection connection = null;
    private Statement statement = null;

    public JDBCClient() throws ClassNotFoundException, SQLException {
        Class.forName(JDBC_DRIVER);
        System.out.println("Драйвер подключен");
        connection = DriverManager.getConnection(DB_URL);
        System.out.println("База Подключена!");
        statement = connection.createStatement();
//          Создание таблицы Категории
        statement.execute(category);
        System.out.println("Таблица категории создана!");
//          Создание таблицы Фильтры
        statement.execute(filter);
        System.out.println("Таблица фильтры создана!");
//          Создание таблицы Объявления
        statement.execute(city);
        System.out.println("Таблица города создана!");
//          Создание таблицыГорода
        statement.execute(ad);
        System.out.println("Таблица объявления создана!");
    }

    public void filterAdd(String city, String category, String subcategory, Integer startPrice, Integer finishPrice,
                          boolean isPhoto, String filterURL) throws SQLException {
        String query = "INSERT INTO 'filter' ('city', 'category', 'subcategory', 'startPrice', 'finishPrice', "
                + "'isPhoto', 'filterURL') VALUES ('" + city + "', '" + category + "','" + subcategory + "','"
                + startPrice + "','" + finishPrice + "','" + isPhoto + "','" + filterURL +"'); ";
        statement.execute(query);
    }

    public void filterDelete(String filterURL) throws SQLException {
        Filter filter = new Filter();
        int check = 0;
        ResultSet resultSet = statement.executeQuery("SELECT 'id' FROM 'filter' WHERE 'filterURL' = '" + filterURL + "';");
        while (resultSet.next()) {
            check = resultSet.getInt("id");
        }
        System.out.println(check);
        String query = "DELETE from 'filter' WHERE 'id' = " + check + ";";
        statement.execute(query);
    }

    public void filterUpdate(int id, String city, String category, String subcategory, Integer startPrice, Integer finishPrice,
                             boolean isPhoto, String filterURL) throws SQLException {
        String query = "UPDATE 'filter' SET 'city' = '" + city + "', 'category' = '" + category + "', 'subcategory' = '"
                + subcategory + "', 'startPrice' = " + startPrice + ", 'finishPrice' = " + finishPrice + ", 'isPhoto' = "
                + isPhoto + ", 'filterURL' = '" + filterURL + "' WHERE 'id' =; " ;
        statement.execute(query);
    }
//
//    public Filter filterSelect(int id) throws SQLException {
//        String query = "SELECT id, name, priceFirst, priceSecond, city, category, subcategory, picture " +
//                "FROM filter WHERE ID = " + id + ";" ;
//        ResultSet resultSet = doExecQuery(connection, query);
//        Filter filter = new Filter();
//        while (resultSet.next()) {
//            filter.setId(resultSet.getInt("id"));
//            filter.setName(resultSet.getString("name"));
//            filter.setPriceFirst(resultSet.getDouble("priceFirst"));
//            filter.setPriceSecond(resultSet.getDouble("priceSecond"));
//            filter.setCity(resultSet.getString("city"));
//            filter.setCategory(resultSet.getString("category"));
//            filter.setSubcategory(resultSet.getString("subcategory"));
//            filter.setPicture(resultSet.getBoolean("picture"));
//        }
//        return filter;
//    }
//
//    public ArrayList<Filter> filterSelectAll() throws SQLException {
//        ArrayList<Filter> arrayList = new ArrayList<Filter>();
//        String query = "SELECT * FROM filter;";
//        ResultSet resultSet = doExecQuery(connection, query);
//        while (resultSet.next()) {
//            Filter filter = new Filter();
//            filter.setId(resultSet.getInt("id"));
//            filter.setName(resultSet.getString("name"));
//            filter.setPriceFirst(resultSet.getDouble("priceFirst"));
//            filter.setPriceSecond(resultSet.getDouble("priceSecond"));
//            filter.setCity(resultSet.getString("city"));
//            filter.setCategory(resultSet.getString("category"));
//            filter.setSubcategory(resultSet.getString("subcategory"));
//            filter.setPicture(resultSet.getBoolean("picture"));
//            arrayList.add(filter);
//        }
//        return arrayList;
//    }
//
    public void categoryAdd(String name, String url, String parent) throws SQLException {
        String query = "INSERT INTO category (name, url, parent) VALUES ('" + name + "','" + url + "','" + parent + "');";
        statement.execute(query);
    }

    //
//    public void categoryDelete(int id) throws SQLException {
//        String query = "DELETE from category WHERE ID=" + id + ";";
//        try {
//            statement = connection.createStatement();
//            doUpdateQuery(connection, query);
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        } /*finally {
//            if (statement != null) {
//                statement.close();
//            }
//            if (connection != null) {
//                connection.close();
//            }*/
//    }
//
    public void categoryUpdate(int id, String name, String url, String parent) throws SQLException {
        String query = "UPDATE category SET name = '" + name + "', url = '" + url + "', parent = '" + parent
                + "' WHERE ID = " + id + ";";
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

    //
//    public ArrayList<Category> categorySelectAll() throws SQLException {
//        ArrayList<Category> arrayList = new ArrayList<>();
//        String query = "SELECT * FROM category;";
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
//        return arrayList;
//    }
//
    public void cityAdd(String name, String url) throws SQLException {
        String query = "INSERT INTO city (name, url) VALUES ('" + name + "','" + url + "');";
        statement.execute(query);
    }
//
//    public void cityUpdate(int id, String name, String url, String parent) throws SQLException {
//        String query = "UPDATE city SET name = '" + name + "', url = '" + url + "', parent = '" + parent
//                + "' WHERE ID = " + id + ";";
//        try {
//            statement = connection.createStatement();
//            doUpdateQuery(connection, query);
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        } /*finally {
//            if (statement != null) {
//                statement.close();
//            }
//            if (connection != null) {
//                connection.close();
//            }
//        }*/
//    }
//
//    public City citySelect(int id) throws SQLException {
//        String query = "SELECT id, name, url, parent FROM city WHERE ID = " + id + ";";
//        ResultSet resultSet = doExecQuery(connection, query);
//        City city = new City();
//        while (resultSet.next()) {
//            city.setId(resultSet.getInt("id"));
//            city.setName(resultSet.getString("name"));
//            city.setUrl(resultSet.getString("url"));
//            city.setParent(resultSet.getString("parent"));
//        }
//        return city;
//    }
//
//    public ArrayList<City> citySelectAll() throws SQLException {
//        ArrayList<City> arrayList = new ArrayList<City>();
//        String query = "SELECT * FROM city;";
//        Statement statement = connection.createStatement();
//        ResultSet resultSet = statement.executeQuery(query);
//        while (resultSet.next()) {
//            City city = new City();
//            city.setId(resultSet.getInt("id"));
//            city.setName(resultSet.getString("name"));
//            city.setUrl(resultSet.getString("url"));
//            city.setParent(resultSet.getString("parent"));
//            arrayList.add(city);
//        }
//        return arrayList;
//    }
//
//    public void commentAdd(String url, String description) throws SQLException {
//        String query = "INSERT INTO comment (url, description) VALUES ('" + url + "','" + description + "');";
//        try {
//            statement = connection.createStatement();
//            doUpdateQuery(connection, query);
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        } /*finally {
//            if (statement != null) {
//                statement.close();
//            }
//            if (connection != null) {
//                connection.close();
//            }
//        }*/
//    }
//
//    public void commentDelete(int id) throws SQLException {
//        String query = "DELETE from comment WHERE ID=" + id + ";";
//        try {
//            statement = connection.createStatement();
//            doUpdateQuery(connection, query);
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        } /*finally {
//            if (statement != null) {
//                statement.close();
//            }
//            if (connection != null) {
//                connection.close();
//            }
//        }*/
//    }
//
//    public void commentUpdate(int id, String url, String description) throws SQLException {
//        String query = "UPDATE comment SET url = '" + url + "', description = '" + description + "' WHERE ID = " + id + ";";
//        try {
//            statement = connection.createStatement();
//            doUpdateQuery(connection, query);
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        } /*finally {
//            if (statement != null) {
//                statement.close();
//            }
//            if (connection != null) {
//                connection.close();
//            }
//        }*/
//    }
//
//    public Comment commentSelect(int id) throws SQLException {
//        String query = "SELECT id, name, url, parent FROM comment WHERE ID = " + id + ";";
//        ResultSet resultSet = doExecQuery(connection, query);
//        Comment comment = new Comment();
//        while (resultSet.next()) {
//            comment.setId(resultSet.getInt("id"));
//            comment.setUrl(resultSet.getString("url"));
//            comment.setDescription(resultSet.getString("description"));
//        }
//        return comment;
//    }
//
//    public ArrayList<Comment> commentSelectAll() throws SQLException {
//        ArrayList<Comment> arrayList = new ArrayList<Comment>();
//        String query = "SELECT * FROM comment;";
//        ResultSet resultSet = doExecQuery(connection, query);
//        while (resultSet.next()) {
//            Comment comment = new Comment();
//            comment.setId(resultSet.getInt("id"));
//            comment.setUrl(resultSet.getString("url"));
//            comment.setDescription(resultSet.getString("description"));
//            arrayList.add(comment);
//        }
//        return arrayList;
//    }
}
