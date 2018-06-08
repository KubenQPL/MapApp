package pl.jakubneukirch.mapapp.di;

import dagger.Subcomponent;
import pl.jakubneukirch.mapapp.main.MainActivity;

@Subcomponent(modules = {ActivityModule.class})
public interface ActivityComponent {
    void inject(MainActivity activity);
}
