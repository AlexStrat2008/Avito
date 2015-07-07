package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.util.Duration;
import sample.api.AvitoAd;
import sample.controllers.FilterController;
import sample.controllers.MainController;
import sample.dbclasses.JDBCClient;
import sample.models.Filter;
import sample.parse.Parse;

import sample.trey.MyTrayIcon;
import sample.services.AvitoAdsSuperService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;

public class App extends Application {

    public Filter getFilter() {
        return filter;
    }

    private  Filter filter;

    public ObservableList<AvitoAd> getAdsObservableList() {
        return adsObservableList;
    }

    private ObservableList<AvitoAd> adsObservableList = FXCollections.observableArrayList();
    private MyTrayIcon myTrayIcon;
    private AvitoAdsSuperService avitoAdsService;

    public final static Duration ServiceRequestPeriod = Duration.minutes(1);

    private Stage primaryStage;

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Platform.setImplicitExit(false);
        this.primaryStage = primaryStage;
        myTrayIcon = new MyTrayIcon(this);
        refreshApp();
    }

    public void refreshApp() {
        try {
            primaryStage.close();
            adsObservableList.clear();
            if(loadFilter()) {
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
            Parent root = (Parent)loader.load();

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

    public void openMainWindow() throws Exception{
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("view/main.fxml"));
            Parent root = (Parent)loader.load();
            this.primaryStage.setTitle("Avito monitor");
            this.primaryStage.setScene(new Scene(root));

            MainController controller = loader.getController();
            controller.setApp(this);

            this.primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void restartAdsService() throws ClassNotFoundException, SQLException{
        if (avitoAdsService != null) avitoAdsService.cancel();
        avitoAdsService = new AvitoAdsSuperService(filter);
        avitoAdsService.setPeriod(ServiceRequestPeriod);
        avitoAdsService.setDelay(Duration.seconds(1));
        avitoAdsService.getNewDataList().addListener(new ListChangeListener<AvitoAd>() {
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
        });
        avitoAdsService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                int size = ((ObservableList) event.getSource().getValue()).size();
                if (size > 0) {
                    myTrayIcon.newAd(((ObservableList) event.getSource().getValue()).size());
                }

            }
        });
        avitoAdsService.start();
    }

    private boolean loadFilter() throws SQLException, ClassNotFoundException {
        JDBCClient jdbcClient = new JDBCClient();
        if (jdbcClient.isFilterEmpty()) {
            jdbcClient.closeConnection();
            return false;
        } else {
            sample.dbclasses.Filter dbFilter = jdbcClient.getFilterByID(1);
            jdbcClient.closeConnection();
            Filter filter;
            String rawQuery = dbFilter.getFilterURL();
            if (rawQuery != null && !rawQuery.isEmpty()) {
                this.filter = new Filter(rawQuery);
            } else {

                String subcategory = dbFilter.getSubcategory();
                System.out.println(dbFilter.getFinishPrice());
                filter = new Filter(dbFilter.getCity(),
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
