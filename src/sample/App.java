package sample;

import javafx.application.Application;
import javafx.application.HostServices;
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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import sample.api.AvitoAd;
import sample.dbclasses.JDBCClient;
import sample.models.Filter;
import sample.parse.Parse;
import sample.services.AvitoAdsSuperService;
import sample.trey.MyTrayIcon;

import java.io.IOException;
import java.sql.SQLException;

public class App extends Application {

    public static Filter filter = new Filter("rossiya", 0, 0, true, "transport");
    public static ObservableList<AvitoAd> adsObservableList = FXCollections.observableArrayList();
    private static MyTrayIcon myTrayIcon;
    private  static AvitoAdsSuperService avitoAdsService;
    public final static Duration ServiceRequestPeriod = Duration.minutes(1);
    public static HostServices hostServices;

    @Override
    public void start(Stage primaryStage) throws Exception {
        restartAdsService();
        Parent root = FXMLLoader.load(getClass().getResource("/sample/view/main.fxml"));
        primaryStage.setScene(new Scene(root));
        myTrayIcon = new MyTrayIcon();
        myTrayIcon.createTrayIcon(primaryStage);
        hostServices = this.getHostServices();
        primaryStage.show();
    }

    public static void restartAdsService() {
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
        avitoAdsService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                myTrayIcon.newAd(((ObservableList)event.getSource().getValue()).size());
            }
        });
        avitoAdsService.start();
    }

    public static void main(String[] args) {

        try {
            JDBCClient jdbcClient = new JDBCClient();
            System.out.println(jdbcClient.isAdExistsByUrl("qwe"));
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
