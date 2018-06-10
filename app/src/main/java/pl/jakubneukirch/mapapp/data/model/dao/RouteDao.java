package pl.jakubneukirch.mapapp.data.model.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Single;
import pl.jakubneukirch.mapapp.data.model.db.RouteDbEntity;

@Dao
public interface RouteDao {

    @Query("SELECT * FROM routes")
    Single<List<RouteDbEntity>> getAllRoutes();

    @Query("SELECT * FROM routes WHERE id = :routeId")
    Single<RouteDbEntity> getRoute(long routeId);

    @Insert
    long insertRoute(RouteDbEntity route);

}
