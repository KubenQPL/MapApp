package pl.jakubneukirch.mapapp.main;

import android.location.Location;

import com.google.android.gms.maps.LocationSource;

public class MapLocationSource implements LocationSource {

    private OnLocationChangedListener listener;

    public void updateLocation(Location location) {
        listener.onLocationChanged(location);
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        listener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {}
}