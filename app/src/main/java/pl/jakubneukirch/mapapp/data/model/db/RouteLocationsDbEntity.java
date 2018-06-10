package pl.jakubneukirch.mapapp.data.model.db;

import android.arch.persistence.room.Relation;

import java.util.List;

public class RouteLocationsDbEntity {
    private int id;
    private long timestamp;
    @Relation(parentColumn = "id", entityColumn = "route_id")
    private List<LocationDbEntity> locations;

    public RouteLocationsDbEntity(int id, long timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List<LocationDbEntity> getLocations() {
        return locations;
    }

    public void setLocations(List<LocationDbEntity> locations) {
        this.locations = locations;
    }

    public void addLocation(LocationDbEntity location) {
        locations.add(location);
    }
}
