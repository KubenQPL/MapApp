package pl.jakubneukirch.mapapp.di;

import android.app.Application;
import android.app.Service;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.location.LocationManager;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import pl.jakubneukirch.mapapp.data.MapDatabase;
import pl.jakubneukirch.mapapp.data.PlacesApi;
import pl.jakubneukirch.mapapp.data.model.dao.LocationDao;
import pl.jakubneukirch.mapapp.data.model.dao.RouteDao;
import pl.jakubneukirch.mapapp.data.model.dao.RouteLocationsDao;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {

    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/";
    public static final String API_KEY = "AIzaSyCJikrT2dph6C2HExX4DF3mLi0plROEmEo";

    private final Application app;

    public AppModule(Application app) {
        this.app = app;
    }

    @Provides
    @AppContext
    Context providesContext() {
        return app.getApplicationContext();
    }

    @Provides
    @Singleton
    LocationManager providesLocationManager(@AppContext Context context) {
        return (LocationManager) context.getSystemService(Service.LOCATION_SERVICE);
    }

    @Provides
    @Singleton
    MapDatabase providesMapDatabase(@AppContext Context context) {
        return Room.databaseBuilder(context, MapDatabase.class, "map_database").build();
    }

    @Provides
    @Singleton
    LocationDao providesLocationDao(MapDatabase database) {
        return database.locationDao();
    }

    @Provides
    @Singleton
    RouteDao providesRouteDao(MapDatabase database) {
        return database.routeDao();
    }

    @Provides
    @Singleton
    RouteLocationsDao providesRouteLocationsDao(MapDatabase database) {
        return database.routeLocationsDao();
    }

    @Provides
    @Singleton
    OkHttpClient providesOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                HttpUrl outUrl = chain.request().url().newBuilder()
                        .addQueryParameter("key", API_KEY)
                        .build();
                Request outReq = new Request.Builder()
                        .url(outUrl)
                        .build();
                return chain.proceed(outReq);
            }
        });
        HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
        logger.setLevel(HttpLoggingInterceptor.Level.BASIC);
        builder.addInterceptor(logger);
        return builder.build();
    }

    @Provides
    @Singleton
    Retrofit providesRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();
    }

    @Provides
    @Singleton
    PlacesApi providesPlacesApi(Retrofit retrofit) {
        return retrofit.create(PlacesApi.class);
    }
}
