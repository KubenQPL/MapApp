package pl.jakubneukirch.mapapp.main;

import android.location.Location;

import com.google.android.gms.maps.LocationSource;

public class MapLocationSource implements LocationSource {

    private OnLocationChangedListener listener = null;

    public void updateLocation(Location location) {
        if (listener != null) {
            listener.onLocationChanged(location);
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        listener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
    }
}