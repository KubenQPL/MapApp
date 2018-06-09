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
    private Location lastLocation = null;

    private MapLocationSource mapLocationSource;

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
        if(mapLocationSource == null) {
            setupMapLocationSource();
        }
        view.showMyLocation();
        disposables.add(locationApi.getLocationObservable()
                .doOnNext(this::updateLocation)
                .subscribe());
    }

    private void setupMapLocationSource() {
        mapLocationSource = new MapLocationSource();
    }

    private void updateLocation(Location location) {
        mapLocationSource.updateLocation(location);
        view.setLocation(location);
        if (following) {
            if(locations.size() == 0){
                if(lastLocation == null){
                    lastLocation = location;
                }
                locations.add(new LocationDto(lastLocation.getLatitude(), lastLocation.getLongitude()));
                view.drawPolyLine(lastLocation);
            }
            locations.add(new LocationDto(location.getLatitude(), location.getLongitude()));
            view.drawPolyLine(location);
        }
        lastLocation = location;
    }

    void locationPermissionDenied() {
        view.askForPermissions();
    }

    void mapLoaded() {
        view.setLocationSource(mapLocationSource);
        view.showMyLocation();
    }

    void followingEnded(long timestamp) {
        following = false;
        saveRoute(timestamp);
        view.clearPolyLine();
    }

    private void saveRoute(long timestamp) {
        disposables.add(repository.insertRoute(new RouteDbEntity(timestamp))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
