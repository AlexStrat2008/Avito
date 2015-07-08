package sample.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import sample.App;
import sample.api.*;
import sample.models.Filter;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Comparator;

/**
 * Created by Alexandr on 04.07.2015.
 */
public class AvitoAdsService extends ScheduledService<ObservableList<AvitoAd>>{

    private Filter filter;
    /**
     * ƒата раньше которой объ€влени€ не будут выдаватьс€
     * при приеме очередной порции данных устанавливаетс€ врем€ самого нового
     */
    private LocalDateTime minDateTime;
    /**
     * ”ри последнего прин€того объ€влени€
     */
    private URI lastAdUri;

    public AvitoAdsService(Filter filter, LocalDateTime minDateTime, URI lastAdUri) {
        this.filter = filter;
        this.minDateTime = minDateTime;
        this.lastAdUri = lastAdUri;
    }

    @Override
    protected Task<ObservableList<AvitoAd>> createTask() {
        return new Task<ObservableList<AvitoAd>>() {
            protected ObservableList<AvitoAd> call() {
                AvitoApi avitoApi = new AvitoApi();
                ObservableList<AvitoAd> observableList = FXCollections.observableArrayList();
                try {
                    for (AvitoAd ad : avitoApi.getAds(filter, (a) -> a.equals(a))) {

                        if ((lastAdUri==null || !ad.getURI().toString().equals(lastAdUri.toString()))
                                && (minDateTime==null || ad.getDateTime().compareTo(minDateTime) >=0)) {
                            observableList.add(ad);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                observableList.sort(new Comparator<AvitoAd>() {
                    //более новые в начале
                    @Override
                    public int compare(AvitoAd ad1, AvitoAd ad2) {
                        return ad1.getDateTime().compareTo(ad2.getDateTime()) * (-1);
                    }
                });
                if (observableList.size() > 0) {
                    minDateTime = observableList.get(0).getDateTime();
                    lastAdUri = observableList.get(0).getURI();
                }

                return observableList;
            }
        };
    }
}
