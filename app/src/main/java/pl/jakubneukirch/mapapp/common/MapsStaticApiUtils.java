package pl.jakubneukirch.mapapp.common;

import android.util.Log;

import java.util.List;

import pl.jakubneukirch.mapapp.data.model.db.LocationDbEntity;

public class MapsStaticApiUtils {

    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/staticmap?";
    private static final String API_KEY = "AIzaSyCJikrT2dph6C2HExX4DF3mLi0plROEmEo";
    private static final int IMAGE_WIDTH = 400;
    private static final int IMAGE_HEIGHT = 300;

    public static String getPathUrl(List<LocationDbEntity> locations) {
        StringBuilder builder = new StringBuilder();
        builder.append(BASE_URL);
        builder.append("path=");
        LocationDbEntity location;
        for (int i = 0; i < locations.size(); i++) {
            location = locations.get(i);
            builder.append(location.getLat());
            builder.append(",");
            builder.append(location.getLon());
            if (i < locations.size() - 1) {
                builder.append("|");
            }
        }
        builder.append("&size=");
        builder.append(IMAGE_WIDTH);
        builder.append("x");
        builder.append(IMAGE_HEIGHT);
        builder.append("&key=");
        builder.append(API_KEY);
        return builder.toString();
    }

}
