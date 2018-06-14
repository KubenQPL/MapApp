package pl.jakubneukirch.mapapp.main;

import android.location.Location;

import com.google.android.gms.maps.LocationSource;

import java.util.ArrayList;
import java.util.List;

import pl.jakubneukirch.mapapp.base.MvpView;
import pl.jakubneukirch.mapapp.data.model.api.PlaceDetails;
import pl.jakubneukirch.mapapp.data.model.db.LocationDbEntity;
import pl.jakubneukirch.mapapp.data.model.dto.LocationDto;

public interface MainView extends MvpView {
    void setup();

    void setLocation(Location location);

    void setLocation(LocationDbEntity location);

    void drawPolyLine(Location location);

    void drawPolyLine(LocationDbEntity location);

    void drawPolyLine(double lat, double lon);

    void drawPath(List<LocationDbEntity> list);

    void clearPolyLine();

    void askForPermissions();

    void askForProvider();

    void showMyLocation(boolean show);

    void setLocationSource(LocationSource source);

    void openSavedScreen();

    void setupMovableMap();

    void setupStaticMap();

    void setupFollowingToolbar();

    void setupInfoToolbar();

    void goBack();

    void showPlaces(ArrayList<PlaceDetails> places);

    void setToggleCaptionStatus(boolean on);
}
