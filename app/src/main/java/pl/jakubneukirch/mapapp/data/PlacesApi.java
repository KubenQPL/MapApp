package pl.jakubneukirch.mapapp.data;

import io.reactivex.Single;
import pl.jakubneukirch.mapapp.data.model.api.NearbyPlacesSearch;
import pl.jakubneukirch.mapapp.data.model.api.PlaceDetails;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlacesApi {

    @GET("nearbysearch/json?radius=3000&type=political")
    Single<NearbyPlacesSearch> getNearbyPlaces(@Query("location") String location);

    @GET("details/json?fields=name,formatted_address,photo")
    Single<PlaceDetails> getPlaceDetails(@Query("placeid") String placeId);
}
