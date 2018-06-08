package pl.jakubneukirch.mapapp.di;

import android.app.Application;
import android.content.Context;

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
}
