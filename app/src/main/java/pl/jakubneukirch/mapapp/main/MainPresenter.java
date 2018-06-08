package pl.jakubneukirch.mapapp.main;

import javax.inject.Inject;

import pl.jakubneukirch.mapapp.R;
import pl.jakubneukirch.mapapp.base.BasePresenter;

public class MainPresenter extends BasePresenter<MainView> {
    @Inject
    public MainPresenter() {
    }

    void onCreate() {
        view.showMessage(R.string.test);
    }
}
