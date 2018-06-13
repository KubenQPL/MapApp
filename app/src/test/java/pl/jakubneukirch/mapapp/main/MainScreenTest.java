package pl.jakubneukirch.mapapp.main;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Observable;
import io.reactivex.Single;
import pl.jakubneukirch.mapapp.RxSchedulersOverrideRule;
import pl.jakubneukirch.mapapp.data.LocationApi;
import pl.jakubneukirch.mapapp.data.MapRepository;
import pl.jakubneukirch.mapapp.data.model.db.RouteDbEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MainScreenTest {

    @Rule public final RxSchedulersOverrideRule rxSchedulersOverrideRule = new RxSchedulersOverrideRule();
    @Mock MainView view;
    @Mock LocationApi locationApi;
    @Mock MapRepository repository;

    @InjectMocks
    MainPresenter presenter;


    @Before
    public void setup() {
        presenter.attachView(view);
    }

    @After
    public void tearDown() {
        presenter.detachView();
        presenter.onDestroy();
    }

    @Test
    public void shouldSetupView() {
        presenter.onCreate();

        verify(view).setup();
    }

    @Test
    public void shouldAskForProvider() {
        when(locationApi.isProviderSet()).thenReturn(false);
        when(locationApi.setupProvider()).thenReturn(false);

        presenter.locationPermissionGranted();

        verify(view).askForProvider();
    }

    @Test
    public void shouldShowLocation() {
        when(locationApi.isProviderSet()).thenReturn(false);
        when(locationApi.setupProvider()).thenReturn(true);
        when(locationApi.getLocationObservable()).thenReturn(Observable.empty());

        presenter.locationPermissionGranted();

        verify(view).showMyLocation();
    }

    @Test
    public void shouldObserveLocation() {
        when(locationApi.isProviderSet()).thenReturn(false);
        when(locationApi.setupProvider()).thenReturn(true);
        when(locationApi.getLocationObservable()).thenReturn(Observable.empty());

        presenter.locationPermissionGranted();

        verify(locationApi).getLocationObservable();
    }

    @Test
    public void shouldObserveLocationWithProviderSet() {
        when(locationApi.isProviderSet()).thenReturn(true);
        when(locationApi.getLocationObservable()).thenReturn(Observable.empty());

        presenter.locationPermissionGranted();

        verify(locationApi).getLocationObservable();
    }

    @Test
    public void shouldAskForPermission() {
        presenter.locationPermissionDenied();

        verify(view).askForPermissions();
    }

    @Test
    public void shouldShowLocationMapLoaded() {
        presenter.mapLoaded();

        verify(view).showMyLocation();
    }

    @Test
    public void shouldInsertRoute() {
        final long timestamp = 1L;
        final RouteDbEntity route = new RouteDbEntity(timestamp);
        when(repository.insertRoute(route)).thenReturn(Single.just(0L));

        presenter.followingEnded(timestamp);

        verify(repository).insertRoute(route);
    }

    @Test
    public void shouldOpenSavedScreen() {
        presenter.onItemScreenSelected(MainPresenter.SAVED_ACTIVITY_POSITION);

        verify(view).openSavedScreen();
    }

    @Test
    public void shouldDoNothing() {
        presenter.onItemScreenSelected(MainPresenter.MAIN_ACTIVITY_POSITION);

        verify(view, never()).openSavedScreen();
    }
}