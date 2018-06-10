package pl.jakubneukirch.mapapp.di;

import dagger.Subcomponent;
import pl.jakubneukirch.mapapp.main.MainActivity;
import pl.jakubneukirch.mapapp.saved.SavedActivity;

@Subcomponent(modules = {ActivityModule.class})
public interface ActivityComponent {
    void inject(MainActivity activity);
    void inject(SavedActivity activity);
}
