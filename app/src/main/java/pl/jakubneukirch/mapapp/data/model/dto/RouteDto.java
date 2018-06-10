package pl.jakubneukirch.mapapp.data.model.dto;

import java.util.List;

import pl.jakubneukirch.mapapp.data.model.db.LocationDbEntity;
import pl.jakubneukirch.mapapp.data.model.db.RouteLocationsDbEntity;

public class RouteDto {
    private int id;
    private long timestamp;
    private List<LocationDbEntity> locations;
    private String mapImageUrl = "";

    public RouteDto(int id, long timestamp, List<LocationDbEntity> locations) {
        this.id = id;
        this.timestamp = timestamp;
        this.locations = locations;
    }

    public RouteDto(int id, long timestamp, List<LocationDbEntity> locations, String mapImageUrl) {
        this.id = id;
        this.timestamp = timestamp;
        this.locations = locations;
        this.mapImageUrl = mapImageUrl;
    }

    public int getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public List<LocationDbEntity> getLocations() {
        return locations;
    }

    public String getMapImageUrl() {
        return mapImageUrl;
    }

    public void setMapImageUrl(String mapImageUrl) {
        this.mapImageUrl = mapImageUrl;
    }

    public static RouteDto mapRouteDto(RouteLocationsDbEntity route) {
        return new RouteDto(
                route.getId(),
                route.getTimestamp(),
                route.getLocations()
        );
    }

    public static RouteDto mapRouteDto(RouteLocationsDbEntity route, String mapUrl) {
        return new RouteDto(
                route.getId(),
                route.getTimestamp(),
                route.getLocations(),
                mapUrl
        );
    }
}
