package pl.jakubneukirch.mapapp.data.model.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import pl.jakubneukirch.mapapp.data.model.dto.LocationDto;

@Entity(tableName = "locations")
public class LocationDbEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "route_id")
    private long routeId;

    private float lat;

    private float lon;

    public LocationDbEntity(long routeId, float lat, float lon) {
        this.routeId = routeId;
        this.lat = lat;
        this.lon = lon;
    }

    public static LocationDbEntity mapLocationDbEntity(LocationDto location) {
        return new LocationDbEntity(
                location.getRouteId(),
                location.getLat(),
                location.getLon()
        );
    }

    public static LocationDbEntity mapLocationDbEntity(LocationDto location, long routeId) {
        return new LocationDbEntity(
                routeId,
                location.getLat(),
                location.getLon()
        );
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRouteId() {
        return routeId;
    }

    public void setRouteId(long routeId) {
        this.routeId = routeId;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }
}
