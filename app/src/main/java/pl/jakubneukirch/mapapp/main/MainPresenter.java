package pl.jakubneukirch.mapapp.main;

import android.location.Location;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import pl.jakubneukirch.mapapp.base.BasePresenter;
import pl.jakubneukirch.mapapp.data.LocationApi;
import pl.jakubneukirch.mapapp.data.MapRepository;
import pl.jakubneukirch.mapapp.data.model.api.PlaceDetails;
import pl.jakubneukirch.mapapp.data.model.db.LocationDbEntity;
import pl.jakubneukirch.mapapp.data.model.db.RouteDbEntity;
import pl.jakubneukirch.mapapp.data.model.dto.LocationDto;

import static pl.jakubneukirch.mapapp.main.MainActivity.NO_ROUTE_ID;

public class MainPresenter extends BasePresenter<MainView> {

    public static final int MAIN_ACTIVITY_POSITION = 0;
    public static final int SAVED_ACTIVITY_POSITION = 1;

    private final LocationApi locationApi;
    private final MapRepository repository;

    private final ArrayList<LocationDto> locations = new ArrayList<>();

    private Boolean following = false;
    private Location lastLocation = null;

    private MapLocationSource mapLocationSource = new MapLocationSource();
    private long routeId;
    private List<LocationDbEntity> routeLocations = null;

    @Inject
    public MainPresenter(LocationApi locationApi, MapRepository repository) {
        this.locationApi = locationApi;
        this.repository = repository;
    }

    void onCreate(long routeId) {
        this.routeId = routeId;
        view.setup();
        if (routeId != NO_ROUTE_ID) {
            loadRoute();
            view.setupInfoToolbar();
        } else {
            view.setupFollowingToolbar();
        }
    }

    private void loadRoute() {
        disposables.add(
                repository.getRouteLocations(routeId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSuccess(this::setupRoute)
                        .subscribe()
        );
    }

    private void setupRoute(List<LocationDbEntity> list) {
        this.routeLocations = list;
        updateRouteLocation();
        drawRoute(list);
    }

    private void updateRouteLocation() {
        if (routeLocations != null && routeLocations.size() > 0) {
            view.setLocation(routeLocations.get(0));
        }
    }

    private void drawRoute(List<LocationDbEntity> list) {
        view.drawPath(list);
    }

    void locationPermissionGranted() {
        if (routeId == NO_ROUTE_ID) {
            setupLocationApi();
        }
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
        showMyLocation();
        disposables.add(locationApi.getLocationObservable()
                .doOnNext(this::updateLocation)
                .subscribe());
    }

    private void showMyLocation() {
        Location location = locationApi.getLastKnownLocation();
        view.showMyLocation((routeId == NO_ROUTE_ID));
        if (location != null) {
            updateLocation(location);
        }
    }

    private void updateLocation(Location location) {
        mapLocationSource.updateLocation(location);
        view.setLocation(location);
        if (following) {
            if (locations.size() == 0) {
                if (lastLocation == null) {
                    lastLocation = location;
                }
                addLocation(new LocationDto((float) lastLocation.getLatitude(), (float) lastLocation.getLongitude()));
                view.drawPolyLine(lastLocation);
            }
            addLocation(new LocationDto((float) location.getLatitude(), (float) location.getLongitude()));
            view.drawPolyLine(location);
        }
        lastLocation = location;
    }

    void addLocation(LocationDto location) {
        if (locations.size() > 0) {
            if (!locations.get(locations.size() - 1).equals(location)) {
                locations.add(location);
            }
        } else {
            locations.add(location);
        }
    }

    void locationPermissionDenied() {
        view.askForPermissions();
    }

    void infoItemClicked() {
        if (routeLocations != null && routeLocations.size() > 1) {
            loadRoutePlaces(routeLocations.get(0), routeLocations.get(routeLocations.size() - 1));
        }
    }

    private void loadRoutePlaces(LocationDbEntity locationStart, LocationDbEntity locationEnd) {
        ArrayList<PlaceDetails> places = new ArrayList<>();

        disposables.add(
                getRoutePlaceSingle(locationStart)
                        .concatWith(getRoutePlaceSingle(locationEnd)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(places::add)
                        .doOnComplete(() -> view.showPlaces(places))
                        .doOnError((error) -> Log.d("error", error.getMessage() + ""))
                        .subscribe()
        );
    }

    private Single<PlaceDetails> getRoutePlaceSingle(LocationDbEntity location) {
        return repository.getNearbyPlaces(location.getLat(), location.getLon())
                .subscribeOn(Schedulers.io())
                .flatMap((nearbyPlacesSearch) -> {
                    if (nearbyPlacesSearch.getResults().size() > 0) {
                        return repository.getPlaceDetails(nearbyPlacesSearch.getResults().get(0).getPlaceId());
                    }
                    return Single.just(PlaceDetails.empty());
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    void mapLoaded() {
        if (routeId == NO_ROUTE_ID) {
            view.setupStaticMap();
        } else {
            view.setupMovableMap();
            updateRouteLocation();
        }
        view.setLocationSource(mapLocationSource);
        showMyLocation();
    }

    void followingToggleOff(long timestamp) {
        following = false;
        view.setToggleCaptionStatus(false);
        saveRoute(timestamp);
        view.clearPolyLine();
    }

    private void saveRoute(long timestamp) {
        if (locations.size() > 0) {
            disposables.add(repository.insertRoute(new RouteDbEntity(timestamp))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess(this::saveLocations)
                    .subscribe());
        }
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

    void followingToggleOn() {
        following = true;
        view.setToggleCaptionStatus(true);
    }

    public void itemScreenSelected(int position) {
        if (position == SAVED_ACTIVITY_POSITION) {
            if (routeId == NO_ROUTE_ID) {
                view.openSavedScreen();
            } else {
                view.goBack();
            }
        } else if (position == MAIN_ACTIVITY_POSITION) {
            if (routeId != NO_ROUTE_ID) {
                clear();
                onCreate(NO_ROUTE_ID);
            }
        }
    }

    private void clear() {
        view.clearPolyLine();
    }

    @Override
    public void detachView() {
        view.clearPolyLine();
        super.detachView();
    }
}
