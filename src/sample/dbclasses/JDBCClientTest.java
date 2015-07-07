package sample.dbclasses;

import org.junit.Test;

import static org.junit.Assert.*;
import sample.dbclasses.JDBCClient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Created by tanyastarshova on 07.07.2015.
 */
public class JDBCClientTest {
    Connection connection = null;

    @Test
    public void testDropAllTable() throws Exception {



    }
    @Test
    public void testCloseConnection() throws Exception {


        String DB_URL = "jdbc:sqlite:avitodb.s3db";
        connection = DriverManager.getConnection(DB_URL);

        if(connection.isClosed()){
            System.out.println("Connection is closed");
        }else{
            System.out.println("Connection is opened");
            //Assert.fail("Fail test!");
        }

    }

Statement statement = null;
    @Test
    public void testCloseStatement() throws Exception {

    }

    @Test
    public void testAdDeleteTable() throws Exception {


    }
}