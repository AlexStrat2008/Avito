package sample.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import sample.api.AvitoAd;
import sample.api.AvitoApi;
import sample.dbclasses.JDBCClient;
import sample.models.Filter;

import java.net.URI;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Created by Alexandr on 05.07.2015.
 */
public class AvitoAdsSuperService extends ScheduledService {

    private Filter filter;
    private final static AvitoApi avitoApi = new AvitoApi();
    private ObservableList<AvitoAd> newDataList = FXCollections.observableArrayList();
    private JDBCClient jdbcClient;

    public ObservableList<AvitoAd> getNewDataList() {
        return newDataList;
    }

    public AvitoAdsSuperService(Filter filter) throws ClassNotFoundException, SQLException{
        this.filter = filter;
        this.jdbcClient = new JDBCClient();
    }

    private boolean isNew(AvitoAd ad) {
        return !jdbcClient.isAdExistsByUrl(ad.getURI().toString());
    }

    @Override
    protected Task createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {

                newDataList.clear();

                for (AvitoAd ad : avitoApi.getAdsYield(filter)) {
                    if (isNew(ad)) {
                        System.out.println(ad);
                        newDataList.add(ad);
                        jdbcClient.adAdd(
                                ad.getURI() == null ? "" : ad.getURI().toString(),
                                ad.getName(),
                                ad.getPhoto() == null ? "" : ad.getPhoto().toString(),
                                //блять не могли long в базе сделать
                                ad.getPrice() == null ? 0 : ad.getPrice().intValue(),
                                ad.getDescription(),
                                "",
                                ""
                        );
                    }
                }
                return getNewDataList();
            }
        };
    }
}
