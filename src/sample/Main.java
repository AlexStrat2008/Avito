package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.api.AvitoAd;
import sample.dbclasses.JDBCClient;
import sample.models.Filter;
import sample.parse.Parse;
import sample.services.AvitoAdsService;
import java.sql.SQLException;
import java.util.Comparator;

public class Main extends Application {

    public static Filter filter = new Filter("rossiya", 0, 0, true, "transport");
    public static ObservableList<AvitoAd> adsObservableList = FXCollections.observableArrayList();
    private static AvitoAdsService avitoAdsService;

    @Override
    public void start(Stage primaryStage) throws Exception {
        avitoAdsService.start();

        Parent root = FXMLLoader.load(getClass().getResource("/sample/view/filter.fxml"));
        primaryStage.setTitle("filter");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {

        avitoAdsService = new AvitoAdsService(filter, null, null);
        avitoAdsService.setPeriod(Duration.seconds(60));
        avitoAdsService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("Service refreshed data");
                try {
                    adsObservableList.addAll(avitoAdsService.getValue());
                    adsObservableList.sort(new Comparator<AvitoAd>() {
                        //более новые в начале
                        @Override
                        public int compare(AvitoAd ad1, AvitoAd ad2) {
                            return ad1.getDateTime().compareTo(ad2.getDateTime()) * (-1);
                        }
                    });
                } catch (NullPointerException e) {}
            }
        });
        try {
            JDBCClient jdbcClient = new JDBCClient();
            if(jdbcClient.isCatgoryEmpty())
                Parse.parseCategories(jdbcClient);
            if(jdbcClient.isCityEmpty()){
                Parse.parseCities(jdbcClient);
            }
            jdbcClient.closeStatement();
            jdbcClient.closeConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        launch(args);
    }
}
