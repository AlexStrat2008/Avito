package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.util.Duration;
import sample.api.AvitoAd;
import sample.dbclasses.JDBCClient;
import sample.models.Filter;
import sample.parse.Parse;
import sample.services.AvitoAdsService;
import sample.trey.MyTrayIcon;

import java.sql.SQLException;
import java.util.Comparator;

public class Main extends Application {

    private boolean firstTime;

    public static JDBCClient jdbcClient;

    public static Filter filter = new Filter("rossiya", 0, 0, true, "transport");
    public static ObservableList<AvitoAd> adsObservableList = FXCollections.observableArrayList();
    private  static AvitoAdsService avitoAdsService;
    private static MyTrayIcon myTrayIcon;

    @Override
    public void start(Stage primaryStage) throws Exception {
        avitoAdsService.start();
//        createTrayIcon(primaryStage);
        firstTime = true;
        Platform.setImplicitExit(false);
        Parent root = FXMLLoader.load(getClass().getResource("/sample/view/filter.fxml"));
        Platform.setImplicitExit(false);
        primaryStage.setTitle("filter");
        primaryStage.setScene(new Scene(root));
        myTrayIcon = new MyTrayIcon();
        myTrayIcon.createTrayIcon(primaryStage);
        primaryStage.show();
    }

    public static void main(String[] args) {
        avitoAdsService = new AvitoAdsService(filter, null, null);
        avitoAdsService.setPeriod(Duration.seconds(20));
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
                            myTrayIcon.newAd();
                            return ad1.getDateTime().compareTo(ad2.getDateTime()) * (-1);
                        }
                    });
                } catch (NullPointerException e) {

                }
            }
        });

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
