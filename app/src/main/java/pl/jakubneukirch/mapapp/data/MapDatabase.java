package pl.jakubneukirch.mapapp.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import pl.jakubneukirch.mapapp.data.model.dao.LocationDao;
import pl.jakubneukirch.mapapp.data.model.dao.RouteDao;
import pl.jakubneukirch.mapapp.data.model.dao.RouteLocationsDao;
import pl.jakubneukirch.mapapp.data.model.db.LocationDbEntity;
import pl.jakubneukirch.mapapp.data.model.db.RouteDbEntity;

@Database(entities = {LocationDbEntity.class, RouteDbEntity.class}, version = 1)
public abstract class MapDatabase extends RoomDatabase {
    public abstract LocationDao locationDao();
    public abstract RouteDao routeDao();
    public abstract RouteLocationsDao routeLocationsDao();
}
