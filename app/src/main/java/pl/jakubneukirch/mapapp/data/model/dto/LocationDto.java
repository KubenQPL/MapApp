package pl.jakubneukirch.mapapp.data.model.dto;

public class LocationDto {
    private long routeId;
    private double lat;
    private double lon;

    public LocationDto(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public LocationDto(long routeId, double lat, double lon) {
        this.routeId = routeId;
        this.lat = lat;
        this.lon = lon;
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

    @Override
    public boolean equals(Object obj) {
        return (this.lat == ((LocationDto)obj).lat) && (this.lon == ((LocationDto)obj).lon);
    }
}
