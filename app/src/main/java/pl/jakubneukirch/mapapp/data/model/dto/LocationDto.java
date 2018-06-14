package pl.jakubneukirch.mapapp.data.model.dto;

public class LocationDto {
    private long routeId;
    private float lat;
    private float lon;

    public LocationDto(float lat, float lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public LocationDto(long routeId, float lat, float lon) {
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

    @Override
    public boolean equals(Object obj) {
        return (this.lat == ((LocationDto) obj).lat) && (this.lon == ((LocationDto) obj).lon);
    }
}
