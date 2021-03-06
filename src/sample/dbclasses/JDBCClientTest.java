package sample.dbclasses;

import junit.framework.TestCase;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Created by tanyastarshova on 07.07.2015.
 */
public class JDBCClientTest extends TestCase {

    Connection connection = null;
    Statement statement;

    @Test
    public void testDropAllTable() throws Exception {

    }
    @Test
    public void testCloseConnection() throws Exception {
        connection = DriverManager.getConnection("jdbc:sqlite:avitodb.s3db");
        statement = connection.createStatement();
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
    public void testCloseStatement() throws Exception {
       statement.close();
    }

    @Test
    public void testFilterAdd() throws Exception {

    }

    @Test
     public void testAdDeleteTable() throws Exception {

    }

    @Test
    public void testFilterDeleteTable() throws Exception {

        statement.execute("CREATE TABLE filter1 ('id' INTEGER PRIMARY KEY, " +
                "'name' TEXT, 'url' TEXT, 'parent' TEXT);"); System.out.println("Created");

        statement.execute("DROP TABLE filter1");
        System.out.println("Deleted");
    }
}