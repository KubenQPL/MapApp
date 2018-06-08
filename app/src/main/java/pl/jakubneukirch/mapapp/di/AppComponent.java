package pl.jakubneukirch.mapapp.di;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    ActivityComponent getActivityComponent(ActivityModule module);
}

