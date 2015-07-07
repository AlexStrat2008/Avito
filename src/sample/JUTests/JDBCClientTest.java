package sample.JUTests;

import org.junit.Assert;
import org.junit.*;
import sample.dbclasses.*;

import java.sql.Connection;
import java.sql.DriverManager;

import static org.junit.Assert.*;

/**
 * Created by tanyastarshova on 07.07.2015.
 */
public class JDBCClientTest {

    @Test
    public void testCloseConnection() throws Exception {
        Connection connection = null;
        String DB_URL = "jdbc:sqlite:avitodb.s3db";
        connection = DriverManager.getConnection(DB_URL);

        if(connection.isClosed()){
            System.out.println("Connection is closed");
        }else{
            System.out.println("Connection is opened");
            //Assert.fail("Fail test!");
        }

    }

    @Test
    public void testCityAdd() throws Exception {

    }

    @Test
    public void testGetCityByURL() throws Exception {

    }

    @Test
    public void testCloseStatement() throws Exception {

    }

    @Test
    public void testDropAllTable() throws Exception {

    }

    @Test
    public void testIsCityEmpty() throws Exception {

    }

    @Test
    public void testIsFilterEmpty() throws Exception {

    }

    @Test
    public void testIsAdEmpty() throws Exception {

    }

    @Test
    public void testIsCatgoryEmpty() throws Exception {

    }

    @Test
    public void testFilterAdd() throws Exception {

    }

    @Test
    public void testGetCityIDByURL() throws Exception {

    }

    @Test
    public void testCityDeleteByURL() throws Exception {

    }

    @Test
    public void testCityDeleteByID() throws Exception {

    }

    @Test
    public void testGetCityAll() throws Exception {

    }
}