package pl.jakubneukirch.mapapp.di;

import android.app.Application;
import android.app.Service;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.location.LocationManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pl.jakubneukirch.mapapp.data.MapDatabase;
import pl.jakubneukirch.mapapp.data.model.dao.LocationDao;
import pl.jakubneukirch.mapapp.data.model.dao.RouteDao;
import pl.jakubneukirch.mapapp.data.model.dao.RouteLocationsDao;

@Module
public class AppModule {

    private final Application app;

    public AppModule(Application app) {
        this.app = app;
    }

    @Provides
    @AppContext
    Context providesContext() {
        return app.getApplicationContext();
    }

    @Provides
    @Singleton
    LocationManager providesLocationManager(@AppContext Context context) {
        return (LocationManager) context.getSystemService(Service.LOCATION_SERVICE);
    }

    @Provides
    @Singleton
    MapDatabase providesMapDatabase(@AppContext Context context) {
        return Room.databaseBuilder(context, MapDatabase.class, "map_database").build();
    }

    @Provides
    @Singleton
    LocationDao providesLocationDao(MapDatabase database) {
        return database.locationDao();
    }

    @Provides
    @Singleton
    RouteDao providesRouteDao(MapDatabase database) {
        return database.routeDao();
    }

    @Provides
    @Singleton
    RouteLocationsDao providesRouteLocationsDao(MapDatabase database) {
        return database.routeLocationsDao();
    }
}
