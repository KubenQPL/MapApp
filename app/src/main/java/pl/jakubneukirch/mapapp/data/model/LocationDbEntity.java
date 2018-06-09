package pl.jakubneukirch.mapapp.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import pl.jakubneukirch.mapapp.data.dto.LocationDto;

@Entity(tableName = "locations")
public class LocationDbEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "route_id")
    private long routeId;

    private double lat;

    private double lon;

    public LocationDbEntity(long routeId, double lat, double lon) {
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

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
