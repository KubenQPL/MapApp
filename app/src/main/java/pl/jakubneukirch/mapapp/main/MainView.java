package pl.jakubneukirch.mapapp.main;

import android.location.Location;

import pl.jakubneukirch.mapapp.base.MvpView;

public interface MainView extends MvpView {
    void setup();
    void setLocation(Location location);
    void askForPermissions();
    void askForProvider();
    void showMyLocation();
}
