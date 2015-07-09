package sample;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
import sample.dbclasses.JDBCClient;
import sample.models.Filter;
import sample.parse.Parse;
import sample.services.AvitoAdsSuperService;
import sample.trey.MyTrayIcon;
import sun.util.logging.PlatformLogger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class App extends Application {
    private Filter filter;
    private ObservableList<AvitoAd> adsObservableList = FXCollections.observableArrayList();
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

    public ObservableList<AvitoAd> getAdsObservableList() {
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

    public void restartAdsService() throws ClassNotFoundException, SQLException {
        if (avitoAdsService != null) avitoAdsService.cancel();
        avitoAdsService = new AvitoAdsSuperService(filter);
        avitoAdsService.setPeriod(ServiceRequestPeriod);
        avitoAdsService.setDelay(Duration.seconds(20));
        /*avitoAdsService.getNewDataList().addListener(new ListChangeListener<AvitoAd>() {
            @Override
            public void onChanged(Change<? extends AvitoAd> c) {
                while (c.next()) {
                    if (c.wasAdded()) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                adsObservableList.addAll(c.getAddedSubList());
                            }
                        });
                    }
                }
            }
        });*/
        avitoAdsService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                List<AvitoAd> ads = (List<AvitoAd>) event.getSource().getValue();
                adsObservableList.addAll(ads);
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
}
