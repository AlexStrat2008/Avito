package sample.dbclasses;

import java.sql.*;
import java.util.ArrayList;

public class JDBCClient implements DBFunction {
    static final String JDBC_DRIVER = "org.postgresql.Driver";
    static final String URL = "jdbc:postgresql://localhost:5432/";
    static final String DB_URL = "jdbc:postgresql://localhost:5432/avitodb";
    static final String LOGIN = "postgres";
    static final String PASSWORD = "pass";

    private String createDB = "CREATE DATABASE avitodb ENCODING 'UTF8';";

    private String categoryCT = "CREATE TABLE category (id SERIAL PRIMARY KEY NOT NULL, name TEXT," +
            " url TEXT, parent TEXT);";
    private String cityCT = "CREATE TABLE city (id SERIAL PRIMARY KEY NOT NULL, name TEXT, url TEXT, parent TEXT);";
    private String commentCT = "CREATE TABLE comment (id SERIAL PRIMARY KEY NOT NULL, url TEXT, description TEXT);";
    private String filterCT = "CREATE TABLE filter (id SERIAL PRIMARY KEY NOT NULL, name TEXT," +
            " priceFirst DOUBLE PRECISION, priceSecond DOUBLE PRECISION, city TEXT, category TEXT, " +
            "subcategory TEXT, picture BOOLEAN);";

    private Connection connection = null;
    private Statement statement = null;

    //    Доделать!!! Коряво работает
    public JDBCClient() throws ClassNotFoundException {
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("Драйвер подключен");
            connection = DriverManager.getConnection(DB_URL, LOGIN, PASSWORD);

        } catch (SQLException e) {
            createTables();
        }
    }

    public Connection getDBConnection(String URL, String login, String password) {
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("Драйвер подключен");
            connection = DriverManager.getConnection(URL, login, password);
            return connection;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public ResultSet doExecQuery(Connection connection, String query) throws SQLException {
        statement = connection.createStatement();
        ResultSet result = statement.executeQuery(query);
        statement.close();
        return result;
    }

    public void doQuery(Connection connection, String query) throws SQLException {
        statement = connection.createStatement();
        statement.execute(query);
        statement.close();
    }

    public void doUpdateQuery(Connection connection, String query) throws SQLException {
        statement = connection.createStatement();
        statement.executeUpdate(query);
        statement.close();
    }

    public void createTables() {
        try {
            connection = getDBConnection(URL, LOGIN, PASSWORD);
            doQuery(connection, createDB);
            connection.close();
            connection = getDBConnection(DB_URL, LOGIN, PASSWORD);
            doQuery(connection, categoryCT);
            doQuery(connection, cityCT);
            doQuery(connection, commentCT);
            doQuery(connection, filterCT);
            connection.close();
            connection = getDBConnection(DB_URL, LOGIN, PASSWORD);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void filterAdd(String name, Double priceFirst, Double priceSecond, String city, String category,
                          String subcategory, boolean picture) throws SQLException {
        String query = "INSERT INTO filter (name, priceFirst, priceSecond, city, category, subcategory, picture) " +
                "VALUES ('" + name + "', " + priceFirst + ", " + priceSecond + ", '" + city + "', '" + category + "', '"
                + subcategory + "', " + picture + ");";
        try {
            statement = connection.createStatement();
            doUpdateQuery(connection, query);
            ;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public void filterDelete(int id) throws SQLException {
        String query = "DELETE from filter WHERE ID = " + id + ";";
        try {
            statement = connection.createStatement();
            doUpdateQuery(connection, query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public void filterUpdate(int id, String name, Double priceFirst, Double priceSecond, String city, String category,
                             String subcategory, boolean picture) throws SQLException {
        String query = "UPDATE filter SET name = " + name + ", priceFirst = " + priceFirst + ", priceSecond = "
                + priceSecond + ", city = " + city + ", category = " + category + ", subcategory = " + subcategory
                + ", picture = " + picture + " WHERE ID = " + id + ";";
        try {
            statement = connection.createStatement();
            doUpdateQuery(connection, query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public Filter filterSelect(int id) throws SQLException {
        String query = "SELECT id, name, priceFirst, priceSecond, city, category, subcategory, picture " +
                "FROM filter WHERE ID = " + id + ";";
        ResultSet resultSet = doExecQuery(connection, query);
        Filter filter = new Filter();
        while (resultSet.next()) {
            filter.setId(resultSet.getInt("id"));
            filter.setName(resultSet.getString("name"));
            filter.setPriceFirst(resultSet.getDouble("priceFirst"));
            filter.setPriceSecond(resultSet.getDouble("priceSecond"));
            filter.setCity(resultSet.getString("city"));
            filter.setCategory(resultSet.getString("category"));
            filter.setSubcategory(resultSet.getString("subcategory"));
            filter.setPicture(resultSet.getBoolean("picture"));
        }
        return filter;
    }

    public ArrayList<Filter> filterSelectAll() throws SQLException {
        ArrayList<Filter> arrayList = new ArrayList<Filter>();
        String query = "SELECT * FROM filter;";
        ResultSet resultSet = doExecQuery(connection, query);
        while (resultSet.next()) {
            Filter filter = new Filter();
            filter.setId(resultSet.getInt("id"));
            filter.setName(resultSet.getString("name"));
            filter.setPriceFirst(resultSet.getDouble("priceFirst"));
            filter.setPriceSecond(resultSet.getDouble("priceSecond"));
            filter.setCity(resultSet.getString("city"));
            filter.setCategory(resultSet.getString("category"));
            filter.setSubcategory(resultSet.getString("subcategory"));
            filter.setPicture(resultSet.getBoolean("picture"));
            arrayList.add(filter);
        }
        return arrayList;
    }

    public void categoryAdd(String name, String url, String parent) throws SQLException {
        String query = "INSERT INTO category (name, url, parent) VALUES ('" + name + "','" + url + "','" + parent + "');";
        try {
            statement = connection.createStatement();
            doUpdateQuery(connection, query);
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
//        } finally {
//            if (statement != null) {
//                statement.close();
//            }
//            if (connection != null) {
//                connection.close();
//            }
//        }
    }

    public void categoryDelete(int id) throws SQLException {
        String query = "DELETE from category WHERE ID=" + id + ";";
        try {
            statement = connection.createStatement();
            doUpdateQuery(connection, query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public void categoryUpdate(int id, String name, String url, String parent) throws SQLException {
        String query = "UPDATE category SET name = '" + name + "', url = '" + url + "', parent = '" + parent
                + "' WHERE ID = " + id + ";";
        try {
            statement = connection.createStatement();
            doUpdateQuery(connection, query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public Category categorySelect(int id) throws SQLException {
        String query = "SELECT id, name, url, parent FROM category WHERE ID = " + id + ";";
        ResultSet resultSet = doExecQuery(connection, query);
        Category category = new Category();
        while (resultSet.next()) {
            category.setId(resultSet.getInt("id"));
            category.setName(resultSet.getString("name"));
            category.setURL(resultSet.getString("url"));
            category.setParent(resultSet.getString("parent"));
        }
        return category;
    }

    public boolean isTable(String nameTable) {
        String query = "select * from " + nameTable;
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            resultSet.next();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public ArrayList<Category> categorySelectChild(String parent) throws SQLException {
        ArrayList<Category> arrayList = new ArrayList<>();
        String query = "SELECT id, name, url, parent FROM category WHERE parent = '" + parent + "'";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            Category category = new Category();
            category.setId(resultSet.getInt("id"));
            category.setName(resultSet.getString("name"));
            category.setURL(resultSet.getString("url"));
            category.setParent(resultSet.getString("parent"));
            arrayList.add(category);
        }
        return arrayList;
    }

    public ArrayList<Category> categorySelectParent() throws SQLException {
        ArrayList<Category> arrayList = new ArrayList<>();
        String query = "SELECT id, name, url, parent FROM category WHERE parent = '1'";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            Category category = new Category();
            category.setId(resultSet.getInt("id"));
            category.setName(resultSet.getString("name"));
            category.setURL(resultSet.getString("url"));
            category.setParent(resultSet.getString("parent"));
            arrayList.add(category);
        }
        return arrayList;
    }

    public ArrayList<Category> categorySelectAll() throws SQLException {
        ArrayList<Category> arrayList = new ArrayList<>();
        String query = "SELECT * FROM category;";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            Category category = new Category();
            category.setId(resultSet.getInt("id"));
            category.setName(resultSet.getString("name"));
            category.setURL(resultSet.getString("url"));
            category.setParent(resultSet.getString("parent"));
            arrayList.add(category);
        }
        return arrayList;
    }

    public void cityAdd(String name, String url, String parent) throws SQLException {
        String query = "INSERT INTO city (name, url, parent) VALUES ('" + name + "','" + url + "','" + parent + "');";
        try {
            statement = connection.createStatement();
            doUpdateQuery(connection, query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public void cityDelete(int id) throws SQLException {
        String query = "DELETE from city WHERE ID=" + id + ";";
        try {
            statement = connection.createStatement();
            doUpdateQuery(connection, query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public void cityUpdate(int id, String name, String url, String parent) throws SQLException {
        String query = "UPDATE city SET name = '" + name + "', url = '" + url + "', parent = '" + parent
                + "' WHERE ID = " + id + ";";
        try {
            statement = connection.createStatement();
            doUpdateQuery(connection, query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public City citySelect(int id) throws SQLException {
        String query = "SELECT id, name, url, parent FROM city WHERE ID = " + id + ";";
        ResultSet resultSet = doExecQuery(connection, query);
        City city = new City();
        while (resultSet.next()) {
            city.setId(resultSet.getInt("id"));
            city.setName(resultSet.getString("name"));
            city.setURL(resultSet.getString("url"));
            city.setParent(resultSet.getString("parent"));
        }
        return city;
    }

    public ArrayList<City> citySelectAll() throws SQLException {
        ArrayList<City> arrayList = new ArrayList<City>();
        String query = "SELECT * FROM city;";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            City city = new City();
            city.setId(resultSet.getInt("id"));
            city.setName(resultSet.getString("name"));
            city.setURL(resultSet.getString("url"));
            city.setParent(resultSet.getString("parent"));
            arrayList.add(city);
        }
        return arrayList;
    }

    public void commentAdd(String url, String description) throws SQLException {
        String query = "INSERT INTO comment (url, description) VALUES ('" + url + "','" + description + "');";
        try {
            statement = connection.createStatement();
            doUpdateQuery(connection, query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public void commentDelete(int id) throws SQLException {
        String query = "DELETE from comment WHERE ID=" + id + ";";
        try {
            statement = connection.createStatement();
            doUpdateQuery(connection, query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public void commentUpdate(int id, String url, String description) throws SQLException {
        String query = "UPDATE comment SET url = '" + url + "', description = '" + description + "' WHERE ID = " + id + ";";
        try {
            statement = connection.createStatement();
            doUpdateQuery(connection, query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public Comment commentSelect(int id) throws SQLException {
        String query = "SELECT id, name, url, parent FROM comment WHERE ID = " + id + ";";
        ResultSet resultSet = doExecQuery(connection, query);
        Comment comment = new Comment();
        while (resultSet.next()) {
            comment.setId(resultSet.getInt("id"));
            comment.setURL(resultSet.getString("url"));
            comment.setDescription(resultSet.getString("description"));
        }
        return comment;
    }

    public ArrayList<Comment> commentSelectAll() throws SQLException {
        ArrayList<Comment> arrayList = new ArrayList<Comment>();
        String query = "SELECT * FROM comment;";
        ResultSet resultSet = doExecQuery(connection, query);
        while (resultSet.next()) {
            Comment comment = new Comment();
            comment.setId(resultSet.getInt("id"));
            comment.setURL(resultSet.getString("url"));
            comment.setDescription(resultSet.getString("description"));
            arrayList.add(comment);
        }
        return arrayList;
    }
}
