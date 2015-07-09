package sample.services;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import sample.api.AvitoAd;
import sample.api.AvitoApi;
import sample.dbclasses.JDBCClient;
import sample.models.Filter;

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexandr on 05.07.2015.
 */
public class AvitoAdsSuperService extends ScheduledService {

    private Filter filter;
    private AvitoApi avitoApi = new AvitoApi();
    private JDBCClient jdbcClient;

    public AvitoAdsSuperService(Filter filter) throws ClassNotFoundException, SQLException{
        this.filter = filter;
    }

    private boolean isNew(AvitoAd ad) {
        return !jdbcClient.isAdExistsByUrl(ad.getURI().toString());
    }

    @Override
    protected Task createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                jdbcClient = new JDBCClient();
                List<AvitoAd> ads = avitoApi.getAds(filter, (ad) -> isNew(ad));
                ads.forEach(ad -> {
                    try {
                        jdbcClient.adAdd(
                                ad.getURI() == null ? "" : ad.getURI().toString(),
                                ad.getName(),
                                ad.getPhoto() == null ? "" : ad.getPhoto().toString(),
                                ad.getPrice() == null ? 0 : ad.getPrice().intValue(),
                                ad.getDescription(),
                                "",
                                "",false
                        );
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                });
                jdbcClient.closeStatement();
                jdbcClient.closeConnection();
                return ads;
            }
        };
    }
}
