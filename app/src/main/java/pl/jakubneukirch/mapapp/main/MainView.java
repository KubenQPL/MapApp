package pl.jakubneukirch.mapapp.main;

import android.location.Location;

import com.google.android.gms.maps.LocationSource;

import pl.jakubneukirch.mapapp.base.MvpView;

public interface MainView extends MvpView {
    void setup();

    void setLocation(Location location);

    void drawPolyLine(Location location);

    void clearPolyLine();

    void askForPermissions();

    void askForProvider();

    void showMyLocation();

    void setLocationSource(LocationSource source);

    void openSavedScreen();
}
