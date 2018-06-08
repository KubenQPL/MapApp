package pl.jakubneukirch.mapapp.main;

import android.location.Location;

import javax.inject.Inject;

import pl.jakubneukirch.mapapp.base.BasePresenter;
import pl.jakubneukirch.mapapp.data.LocationApi;

public class MainPresenter extends BasePresenter<MainView> {

    private final LocationApi locationApi;

    @Inject
    public MainPresenter(LocationApi locationApi) {
        this.locationApi = locationApi;
    }

    void onCreate() {
        view.setup();
    }

    void locationPermissionGranted() {
        setupLocationApi();
    }

    private void setupLocationApi() {
        if (!locationApi.isProviderSet()) {
            if (locationApi.setupProvider()) {
                startLocationUpdates();
            } else {
                view.askForProvider();
            }
        } else {
            startLocationUpdates();
        }
    }

    private void startLocationUpdates() {
        disposables.add(locationApi.getLocationObservable()
                .doOnNext((Location location) -> view.setLocation(location))
                .subscribe());
    }

    void locationPermissionDenied() {
        view.askForPermissions();
    }
}
