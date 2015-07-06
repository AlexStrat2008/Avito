package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.util.Duration;
import sample.api.AvitoAd;
import sample.dbclasses.JDBCClient;
import sample.models.Filter;
import sample.parse.Parse;

import sample.trey.MyTrayIcon;
import sample.services.AvitoAdsSuperService;

import java.sql.SQLException;
import java.util.Comparator;

public class App extends Application {

    public static Filter filter = new Filter("/rossiya/koshki");
    public static ObservableList<AvitoAd> adsObservableList = FXCollections.observableArrayList();
    private static MyTrayIcon myTrayIcon;
    private  static AvitoAdsSuperService avitoAdsService;
    public final static Duration ServiceRequestPeriod = Duration.minutes(1);

    @Override
    public void start(Stage primaryStage) throws Exception {
        restartAdsService();
        Parent root = FXMLLoader.load(getClass().getResource("/sample/view/main.fxml"));
        primaryStage.setScene(new Scene(root));
        myTrayIcon = new MyTrayIcon();
        myTrayIcon.createTrayIcon(primaryStage);
        primaryStage.show();
    }

    public static void restartAdsService() throws ClassNotFoundException, SQLException{
        if (avitoAdsService != null) avitoAdsService.cancel();
        avitoAdsService = new AvitoAdsSuperService(filter);
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
            if(jdbcClient.isCategoryEmpty())
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
