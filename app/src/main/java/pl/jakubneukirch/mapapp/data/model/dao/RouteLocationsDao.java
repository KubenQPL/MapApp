package pl.jakubneukirch.mapapp.data.model.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Single;
import pl.jakubneukirch.mapapp.data.model.db.RouteLocationsDbEntity;

@Dao
public interface RouteLocationsDao {
    @Query("SELECT id, timestamp FROM routes")
    Single<List<RouteLocationsDbEntity>> getRoutesWithLocations();
}
