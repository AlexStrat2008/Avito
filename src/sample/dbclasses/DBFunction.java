package sample.dbclasses;

import java.sql.*;
import java.util.ArrayList;

public interface DBFunction {
    String JDBC_DRIVER = "org.postgresql.Driver";
    String URL = "jdbc:postgresql://localhost:5432/";
    String DB_URL = "jdbc:postgresql://localhost:5432/avitodb";
    String LOGIN = "postgres";
    String PASSWORD = "147432";

    String createDB = "CREATE DATABASE avitodb ENCODING 'UTF8';";

    String categoryCT = "CREATE TABLE category (id SERIAL PRIMARY KEY NOT NULL, name TEXT," +
            " url TEXT, parent TEXT);";
    String cityCT = "CREATE TABLE city (id SERIAL PRIMARY KEY NOT NULL, name TEXT, url TEXT, parent TEXT);";
    String commentCT = "CREATE TABLE comment (id SERIAL PRIMARY KEY NOT NULL, url TEXT, description TEXT);";
    String filterCT = "CREATE TABLE filter (id SERIAL PRIMARY KEY NOT NULL, name TEXT," +
            " priceFirst DOUBLE PRECISION, priceSecond DOUBLE PRECISION, city TEXT, category TEXT, subcategory TEXT);";

    Connection connection = null;
    Statement statement = null;

    Connection getDBConnection(String URL, String login, String password);

    ResultSet doExecQuery(Connection connection, String query) throws SQLException;
    void doQuery(Connection connection, String query) throws SQLException;
    void doUpdateQuery(Connection connection, String query) throws SQLException;

    void createTables() throws SQLException;

    void filterAdd(String _name, Double _priceFirst, Double _priceSecond, String city, String category,
                          String subcategory, boolean picture) throws SQLException;
    void filterDelete(int id) throws SQLException;
    void filterUpdate(int id, String name, Double priceFirst, Double priceSecond, String city, String category,
                            String subcategory, boolean picture) throws SQLException;
    Filter filterSelect(int id) throws SQLException;
    ArrayList<Filter> filterSelectAll() throws SQLException;

    void categoryAdd(String name, String url, String parent) throws SQLException;
    void categoryDelete(int id) throws SQLException;
    void categoryUpdate(int id, String name, String url, String parent) throws SQLException;
    Category categorySelect(int id) throws SQLException;
    ArrayList<Category> categorySelectAll() throws SQLException;

    void cityAdd(String name, String url, String parent) throws SQLException;
    void cityDelete(int id) throws SQLException;
    void cityUpdate(int id, String name, String url, String parent) throws SQLException;
    City citySelect(int id) throws SQLException;
    ArrayList<City> citySelectAll() throws SQLException;

    void commentAdd(String url, String description) throws SQLException;
    void commentDelete(int id) throws SQLException;
    void commentUpdate(int id, String url, String description) throws SQLException;
    Comment commentSelect(int id) throws SQLException;
    ArrayList<Comment> commentSelectAll() throws SQLException;
}
