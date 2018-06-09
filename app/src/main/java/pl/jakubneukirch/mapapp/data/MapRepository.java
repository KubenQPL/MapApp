package pl.jakubneukirch.mapapp.data;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import pl.jakubneukirch.mapapp.data.dao.LocationDao;
import pl.jakubneukirch.mapapp.data.dao.RouteDao;
import pl.jakubneukirch.mapapp.data.model.LocationDbEntity;
import pl.jakubneukirch.mapapp.data.model.RouteDbEntity;

public class MapRepository {
    private final LocationDao locationDao;
    private final RouteDao routeDao;

    @Inject
    public MapRepository(LocationDao locationDao, RouteDao routeDao) {
        this.locationDao = locationDao;
        this.routeDao = routeDao;
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

}
