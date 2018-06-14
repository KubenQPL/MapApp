package pl.jakubneukirch.mapapp.common;

import pl.jakubneukirch.mapapp.di.AppModule;

public class PlacesUtils {

    private static final String PHOTOS_URL = "https://maps.googleapis.com/maps/api/place/photo?";
    private static final int DEFAULT_MAX_WIDTH = 800;

    public static String getPhotoUrl(String photoReference, int maxWidth) {
        return PHOTOS_URL + "maxwidth=" + maxWidth +
                "&photoreference=" + photoReference +
                "&key=" + AppModule.API_KEY;
    }

    public static String getPhotoUrl(String photoReference) {
        return getPhotoUrl(photoReference, DEFAULT_MAX_WIDTH);
    }
}
