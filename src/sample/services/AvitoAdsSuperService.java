package sample.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import sample.api.AvitoAd;
import sample.api.AvitoApi;
import sample.models.Filter;

import java.net.URI;
import java.time.LocalDateTime;

/**
 * Created by Alexandr on 05.07.2015.
 */
public class AvitoAdsSuperService extends ScheduledService {

    private Filter filter;
    private LocalDateTime minDateTime;
    private final static AvitoApi avitoApi = new AvitoApi();
    private ObservableList<AvitoAd> newDataList = FXCollections.observableArrayList();

    public ObservableList<AvitoAd> getNewDataList() {
        return newDataList;
    }

    public AvitoAdsSuperService(Filter filter, LocalDateTime minDateTime) {
        this.filter = filter;
        this.minDateTime = minDateTime;
    }

    @Override
    protected Task createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {

                newDataList.clear();

                LocalDateTime newestDateTime = minDateTime;

                for(AvitoAd ad : avitoApi.getAdsYield(filter)) {

                    if (minDateTime == null || ad.getDateTime().compareTo(minDateTime) >=0) {
                        newDataList.add(ad);
                    }

                    if (newestDateTime == null || ad.getDateTime().compareTo(newestDateTime) > 0) {
                        newestDateTime = ad.getDateTime();
                    }
                }

                minDateTime = newestDateTime;
                return getNewDataList();
            }
        };
    }
}
