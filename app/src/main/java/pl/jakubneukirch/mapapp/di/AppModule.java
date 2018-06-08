package pl.jakubneukirch.mapapp.di;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.location.LocationManager;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

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
    LocationManager providesLocationManager(@AppContext Context context) {
        return (LocationManager) context.getSystemService(Service.LOCATION_SERVICE);
    }
}
