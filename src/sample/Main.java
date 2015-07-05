package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
import sample.services.AvitoAdsSuperService;

import java.sql.SQLException;
import java.util.Comparator;

public class Main extends Application {

    public static JDBCClient jdbcClient;

    public static Filter filter = new Filter("rossiya", 0, 0, true, "transport");
    public static ObservableList<AvitoAd> adsObservableList = FXCollections.observableArrayList();
    private  static AvitoAdsSuperService avitoAdsService;
    public final static Duration ServiceRequestPeriod = Duration.minutes(5);

    @Override
    public void start(Stage primaryStage) throws Exception {
        restartAdsService();

        Parent root = FXMLLoader.load(getClass().getResource("/sample/view/main.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void restartAdsService() {
        System.out.println(filter);
        if (avitoAdsService != null) avitoAdsService.cancel();
        avitoAdsService = new AvitoAdsSuperService(filter, null);
        avitoAdsService.setPeriod(ServiceRequestPeriod);
        avitoAdsService.setDelay(Duration.seconds(1));
        avitoAdsService.getNewDataList().addListener(new ListChangeListener<AvitoAd>() {
            @Override
            public void onChanged(Change<? extends AvitoAd> c) {
                while (c.next()) {
                    if (c.wasAdded()) {
                        adsObservableList.addAll(c.getAddedSubList());
                    }
                }

            }
        });
        avitoAdsService.start();
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
