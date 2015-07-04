package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.dbclasses.JDBCClient;
import sample.parse.Parse;

import java.sql.SQLException;

public class Main extends Application {

    public static JDBCClient jdbcClient;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/sample/view/filter.fxml"));
        primaryStage.setTitle("filter");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        try {
            jdbcClient = new JDBCClient();
            if(jdbcClient.isCatgoryEmpty())
                Parse.parseCategories(jdbcClient);
            if(jdbcClient.isCityEmpty()){
                Parse.parseCities(jdbcClient);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        launch(args);
    }
}
