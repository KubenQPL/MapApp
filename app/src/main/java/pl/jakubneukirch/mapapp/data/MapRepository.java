package pl.jakubneukirch.mapapp.data;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import pl.jakubneukirch.mapapp.data.model.api.NearbyPlacesSearch;
import pl.jakubneukirch.mapapp.data.model.api.PlaceDetails;
import pl.jakubneukirch.mapapp.data.model.dao.LocationDao;
import pl.jakubneukirch.mapapp.data.model.dao.RouteDao;
import pl.jakubneukirch.mapapp.data.model.dao.RouteLocationsDao;
import pl.jakubneukirch.mapapp.data.model.db.LocationDbEntity;
import pl.jakubneukirch.mapapp.data.model.db.RouteDbEntity;
import pl.jakubneukirch.mapapp.data.model.db.RouteLocationsDbEntity;

public class MapRepository {
    private final LocationDao locationDao;
    private final RouteDao routeDao;
    private final RouteLocationsDao routeLocationsDao;
    private final PlacesApi placesApi;

    @Inject
    public MapRepository(LocationDao locationDao, RouteDao routeDao, RouteLocationsDao routeLocationsDao, PlacesApi placesApi) {
        this.locationDao = locationDao;
        this.routeDao = routeDao;
        this.routeLocationsDao = routeLocationsDao;
        this.placesApi = placesApi;
    }

    public Completable insertLocation(LocationDbEntity location) {
        return Completable.fromAction(() -> locationDao.insertLocation(location));
    }

    public Completable insertLocations(List<LocationDbEntity> locations) {
        return Completable.fromAction(() -> locationDao.insetLocations(locations));
    }

    public Single<Long> insertRoute(RouteDbEntity route) {
        return Single.create((SingleEmitter<Long> emitter) -> {
            emitter.onSuccess(routeDao.insertRoute(route));
        });
    }

    public Single<List<LocationDbEntity>> getRouteLocations(long routeId) {
        return locationDao.getRouteLocations(routeId);
    }

    public Single<List<RouteDbEntity>> getAllRoutes() {
        return routeDao.getAllRoutes();
    }

    public Single<List<RouteLocationsDbEntity>> getAllRoutesWithLocations() {
        return routeLocationsDao.getRoutesWithLocations();
    }

    public Single<NearbyPlacesSearch> getNearbyPlaces(float lat, float lon) {
        return placesApi.getNearbyPlaces(lat + "," + lon);
    }

    public Single<PlaceDetails> getPlaceDetails(String placeId) {
        return placesApi.getPlaceDetails(placeId);
    }
}
