package pl.jakubneukirch.mapapp.main;

import android.location.Location;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import pl.jakubneukirch.mapapp.base.BasePresenter;
import pl.jakubneukirch.mapapp.data.LocationApi;
import pl.jakubneukirch.mapapp.data.MapRepository;
import pl.jakubneukirch.mapapp.data.dto.LocationDto;
import pl.jakubneukirch.mapapp.data.model.LocationDbEntity;
import pl.jakubneukirch.mapapp.data.model.RouteDbEntity;

public class MainPresenter extends BasePresenter<MainView> {

    private final LocationApi locationApi;
    private final MapRepository repository;

    private final ArrayList<LocationDto> locations = new ArrayList<>();

    private Boolean following = false;
    private Boolean isLocationSet = false;

    @Inject
    public MainPresenter(LocationApi locationApi, MapRepository repository) {
        this.locationApi = locationApi;
        this.repository = repository;
    }

    void onCreate() {
        view.setup();
    }

    void locationPermissionGranted() {
        setupLocationApi();
    }

    private void setupLocationApi() {
        if (!locationApi.isProviderSet()) {
            if (locationApi.setupProvider()) {
                startLocationUpdates();
            } else {
                view.askForProvider();
            }
        } else {
            startLocationUpdates();
        }
    }

    private void startLocationUpdates() {
        view.showMyLocation();
        disposables.add(locationApi.getLocationObservable()
                .doOnNext(this::updateLocation)
                .subscribe());
    }

    private void updateLocation(Location location) {
        if(!isLocationSet){
            view.setLocation(location);
            isLocationSet = true;
        }
        if (following) {
            locations.add(new LocationDto(location.getLatitude(), location.getLongitude()));
        }
    }

    void locationPermissionDenied() {
        view.askForPermissions();
    }

    void mapLoaded() {
        view.showMyLocation();
    }

    void followingEnded(long timestamp) {
        following = false;
        saveRoute(timestamp);
    }

    private void saveRoute(long timestamp) {
        disposables.add(repository.insertRoute(new RouteDbEntity(timestamp))
                .subscribeOn(
                        Schedulers.io()
                )
                .observeOn(
                        AndroidSchedulers.mainThread()
                )
                .doOnSuccess(this::saveLocations)
                .subscribe());
    }

    private void saveLocations(long routeId) {
        final ArrayList<LocationDbEntity> dbLocations = new ArrayList<>();
        for (LocationDto location : locations) {
            dbLocations.add(LocationDbEntity.mapLocationDbEntity(location, routeId));
        }
        locations.clear();
        disposables.add(
                repository.insertLocations(dbLocations)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe()
        );
    }

    void followingBegun() {
        following = true;
    }
}
