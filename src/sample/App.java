package sample;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.api.AvitoAd;
import sample.api.AvitoApi;
import sample.controllers.FilterController;
import sample.controllers.MainController;
import sample.dbclasses.Ad;
import sample.dbclasses.JDBCClient;
import sample.models.Filter;
import sample.models.ListAd;
import sample.parse.Parse;
import sample.services.AvitoAdsSuperService;
import sample.trey.MyTrayIcon;
import sun.util.logging.PlatformLogger;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class App extends Application {
    private Filter filter;
    private ObservableList<ListAd> adsObservableList = FXCollections.observableArrayList();
    private MyTrayIcon myTrayIcon;
    private AvitoAdsSuperService avitoAdsService;
    public static HostServices hostServices;
    public final static Duration ServiceRequestPeriod = Duration.minutes(5);
    private Stage primaryStage;

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        hostServices = this.getHostServices();
        Platform.setImplicitExit(false);
        this.primaryStage = primaryStage;
        myTrayIcon = new MyTrayIcon(this);
        refreshApp();
    }

    public ObservableList<ListAd> getAdsObservableList() {
        return adsObservableList;
    }

    public Filter getFilter() {
        return filter;
    }

    public void refreshApp() {
        try {
            primaryStage.close();
            adsObservableList.clear();
            if (loadFilter()) {
                downloadFromBD();
                restartAdsService();
                openMainWindow();
            } else {
                openFilterWindow();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Critical Error");
            alert.showAndWait();
            System.exit(-1);
        }
    }

    public void openFilterWindow() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("view/filter.fxml"));
            Parent root = (Parent) loader.load();

            Stage filterStage = new Stage();
            filterStage.setTitle("Filter");
            filterStage.setScene(new Scene(root));

            FilterController filterController = loader.getController();
            filterController.setApp(this);

            filterStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openMainWindow() throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("view/main.fxml"));
            Parent root = (Parent) loader.load();
            this.primaryStage.setTitle("Avito monitor");
            this.primaryStage.setScene(new Scene(root));

            MainController controller = loader.getController();
            controller.setApp(this);

            this.primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void downloadFromBD(){
        try {
            JDBCClient jdbcClient = new JDBCClient();
            if(!jdbcClient.isAdEmpty()){
                adsObservableList.addAll(listAdFormAd(jdbcClient.getAdAll().subList(0,25)));
            }
            jdbcClient.closeStatement();
            jdbcClient.closeConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void restartAdsService() throws ClassNotFoundException, SQLException {
        if (avitoAdsService != null) avitoAdsService.cancel();

        avitoAdsService = new AvitoAdsSuperService(filter);
        avitoAdsService.setPeriod(ServiceRequestPeriod);
        avitoAdsService.setDelay(Duration.seconds(10));
        avitoAdsService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                List<AvitoAd> ads = (List<AvitoAd>) event.getSource().getValue();
                adsObservableList.addAll(listAdFormAvitoAd(ads));
                int size = ads.size();
                if (size > 0) {
                    myTrayIcon.newAd(ads.size());
                }
            }
        });
        avitoAdsService.start();
    }

    private boolean loadFilter() throws SQLException, ClassNotFoundException {
        JDBCClient jdbcClient = new JDBCClient();
        if (jdbcClient.isFilterEmpty()) {
            jdbcClient.closeStatement();
            jdbcClient.closeConnection();
            return false;
        } else {
            sample.dbclasses.Filter dbFilter = jdbcClient.getFilterByID(1);
            jdbcClient.closeStatement();
            jdbcClient.closeConnection();
            String rawQuery = dbFilter.getFilterURL();
            if (rawQuery != null && !rawQuery.isEmpty()) {
                this.filter = new Filter(rawQuery);
            } else {
                String subcategory = dbFilter.getSubcategory();
                Filter filter = new Filter(dbFilter.getCity(),
                        dbFilter.getFinishPrice().longValue(),
                        dbFilter.getStartPrice().longValue(),
                        dbFilter.getIsPhoto(),
                        subcategory != null && !subcategory.isEmpty() ?
                                subcategory : dbFilter.getCategory());
                this.filter = filter;
            }
            return true;
        }
    }

    public static void main(String[] args) {
        com.sun.javafx.Logging.getCSSLogger().setLevel(PlatformLogger.Level.OFF);
        try {
            JDBCClient jdbcClient = new JDBCClient();
            if (jdbcClient.isCategoryEmpty())
                Parse.parseCategories(jdbcClient);
            if (jdbcClient.isCityEmpty()) {
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

    private List<ListAd> listAdFormAd(List<Ad> ads) {
        List<ListAd> listAds = new ArrayList<ListAd>();
        ListAd listAd = null;
        for (Ad ad : ads) {
            try {
                listAd = new ListAd(ad.getName(), ad.getPrice().longValue(), new URI(ad.getUrl_photo()), ad.getDescription(), new URI(ad.getUrl()), LocalDateTime.parse("2007-12-03T10:15:30"), ad.getComment(), ad.getPhone(), ad.isFavorit());
                listAds.add(listAd);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return listAds;
    }

    private List<ListAd> listAdFormAvitoAd(List<AvitoAd> ads) {
        List<ListAd> listAds = new ArrayList<ListAd>();
        ListAd listAd = null;
        for (AvitoAd ad : ads) {
                listAd = new ListAd(ad.getName(), ad.getPrice(), ad.getPhoto(), ad.getDescription(), ad.getURI(), ad.getDateTime(), "", "", false);
                listAds.add(listAd);
        }
        return listAds;
    }

}
