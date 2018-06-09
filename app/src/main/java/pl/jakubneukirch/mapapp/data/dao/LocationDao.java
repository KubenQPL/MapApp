package pl.jakubneukirch.mapapp.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Single;
import pl.jakubneukirch.mapapp.data.model.LocationDbEntity;

@Dao
public interface LocationDao {

    @Query("SELECT * FROM locations WHERE route_id = :routeId")
    Single<List<LocationDbEntity>> getRouteLocations(long routeId);

    @Insert
    void insertLocation(LocationDbEntity location);

    @Insert
    void insetLocations(List<LocationDbEntity> locations);
}
