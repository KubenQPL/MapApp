package pl.jakubneukirch.mapapp.data;

import android.annotation.SuppressLint;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;

public class LocationApi {

    private static final long LOCATION_UPDATES_INTERVAL = 0;
    private static final float LOCATION_UPDATES_MIN_DISTANCE = 0f;

    private final LocationManager locationManager;
    private LocationProvider provider;
    private Observable<Location> locationObservable;

    @Inject
    LocationApi(LocationManager locationManager) {
        this.locationManager = locationManager;
        setupProvider();
    }

    public Boolean setupProvider() {
        final String providerName = locationManager.getBestProvider(getCriteria(), true);
        if (providerName == null) {
            provider = null;
            return false;
        } else {
            provider = locationManager.getProvider(providerName);
            setupLocationObservable();
            return true;
        }
    }

    @SuppressLint("MissingPermission")
    private void setupLocationObservable() {
        locationObservable = Observable.create((ObservableEmitter<Location> emitter) -> {
            LocationListener listener = new LocationListener(emitter);
            locationManager.requestSingleUpdate(provider.getName(), listener, null);
            locationManager.requestLocationUpdates(
                    provider.getName(),
                    LOCATION_UPDATES_INTERVAL,
                    LOCATION_UPDATES_MIN_DISTANCE,
                    listener
            );
        });
    }

    @SuppressLint("MissingPermission")
    public Location getLastKnownLocation() {
        if(provider != null){
            return locationManager.getLastKnownLocation(provider.getName());
        }
        return null;
    }

    public Observable<Location> getLocationObservable() {
        return locationObservable;
    }

    public Boolean isProviderSet() {
        return provider != null;
    }

    private Criteria getCriteria() {
        final Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
        return criteria;
    }

    class LocationListener implements android.location.LocationListener {
        private final ObservableEmitter<Location> emitter;

        LocationListener(ObservableEmitter<Location> emitter) {
            this.emitter = emitter;
        }

        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                emitter.onNext(location);
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            emitter.onComplete();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }
    }


}
