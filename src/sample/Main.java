package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.dbclasses.JDBCClient;

public class Main extends Application {

    public static JDBCClient jdbcClient;

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("/sample/view/filter.fxml"));
        primaryStage.setTitle("filter");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {

        try {
            jdbcClient = new JDBCClient();
        } catch (ClassNotFoundException e) {
            System.out.println(e.getException());
        }
        launch(args);
    }
}
