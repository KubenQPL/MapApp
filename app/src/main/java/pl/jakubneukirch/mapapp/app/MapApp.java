package pl.jakubneukirch.mapapp.app;

import android.app.Application;

import pl.jakubneukirch.mapapp.di.AppComponent;
import pl.jakubneukirch.mapapp.di.AppModule;
import pl.jakubneukirch.mapapp.di.DaggerAppComponent;

public class MapApp extends Application {

    public static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }
}
