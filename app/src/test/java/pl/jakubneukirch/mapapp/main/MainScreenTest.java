package pl.jakubneukirch.mapapp.main;

import android.location.Location;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Single;
import pl.jakubneukirch.mapapp.RxSchedulersOverrideRule;
import pl.jakubneukirch.mapapp.data.LocationApi;
import pl.jakubneukirch.mapapp.data.MapRepository;
import pl.jakubneukirch.mapapp.data.model.db.LocationDbEntity;
import pl.jakubneukirch.mapapp.data.model.db.RouteDbEntity;
import pl.jakubneukirch.mapapp.data.model.dto.LocationDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static pl.jakubneukirch.mapapp.main.MainActivity.NO_ROUTE_ID;

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
        Mockito.reset(view);
        Mockito.reset(repository);
    }

    @Test
    public void shouldSetupView() {
        presenter.onCreate(NO_ROUTE_ID);

        verify(view).setup();
    }

    @Test
    public void shouldSetupInfoToolbar() {
        when(repository.getRouteLocations(1)).thenReturn(Single.just(new ArrayList<>()));
        presenter.onCreate(1);

        verify(view).setupInfoToolbar();
    }

    @Test
    public void shouldSetupFollowingToolbar() {
        presenter.onCreate(NO_ROUTE_ID);

        verify(view).setupFollowingToolbar();
    }

    @Test
    public void shouldAskForProvider() {
        when(locationApi.isProviderSet()).thenReturn(false);
        when(locationApi.setupProvider()).thenReturn(false);

        presenter.onCreate(NO_ROUTE_ID);
        presenter.locationPermissionGranted();

        verify(view).askForProvider();
    }

    @Test
    public void shouldShowLocation() {
        when(locationApi.isProviderSet()).thenReturn(false);
        when(locationApi.setupProvider()).thenReturn(true);
        when(locationApi.getLocationObservable()).thenReturn(Observable.empty());

        presenter.onCreate(NO_ROUTE_ID);
        presenter.locationPermissionGranted();

        verify(view).showMyLocation(true);
    }

    @Test
    public void shouldNotShowLocation() {
        when(repository.getRouteLocations(1)).thenReturn(Single.just(new ArrayList<>()));

        presenter.onCreate(1);
        presenter.locationPermissionGranted();

        verify(view, never()).showMyLocation(true);
    }

    @Test
    public void shouldObserveLocation() {
        when(locationApi.isProviderSet()).thenReturn(false);
        when(locationApi.setupProvider()).thenReturn(true);
        when(locationApi.getLocationObservable()).thenReturn(Observable.empty());

        presenter.onCreate(NO_ROUTE_ID);
        presenter.locationPermissionGranted();

        verify(locationApi).getLocationObservable();
    }

    @Test
    public void shouldObserveLocationWithProviderSet() {
        when(locationApi.isProviderSet()).thenReturn(true);
        when(locationApi.getLocationObservable()).thenReturn(Observable.empty());

        presenter.onCreate(NO_ROUTE_ID);
        presenter.locationPermissionGranted();

        verify(locationApi).getLocationObservable();
    }

    @Test
    public void shouldNotObserveLocation() {
        presenter.onCreate(NO_ROUTE_ID);
        presenter.locationPermissionGranted();

        verify(locationApi, never()).getLocationObservable();
    }

    @Test
    public void shouldAskForPermission() {
        presenter.locationPermissionDenied();

        verify(view).askForPermissions();
    }

    @Test
    public void shouldShowLocationMapLoaded() {
        presenter.onCreate(NO_ROUTE_ID);
        presenter.mapLoaded();

        verify(view).showMyLocation(true);
    }

    @Test
    public void shouldInsertRoute() {
        final long timestamp = 1L;
        final RouteDbEntity route = new RouteDbEntity(timestamp);
        when(repository.insertRoute(route)).thenReturn(Single.just(0L));

        presenter.addLocation(new LocationDto(0.0f,0.1f));
        presenter.addLocation(new LocationDto(0.0f,0.1f));
        presenter.followingToggleOff(timestamp);

        verify(repository).insertRoute(route);
    }

    @Test
    public void shouldOpenSavedScreen() {
        presenter.onCreate(NO_ROUTE_ID);
        presenter.itemScreenSelected(MainPresenter.SAVED_ACTIVITY_POSITION);

        verify(view).openSavedScreen();
    }

    @Test
    public void shouldDoNothing() {
        presenter.itemScreenSelected(MainPresenter.MAIN_ACTIVITY_POSITION);

        verify(view, never()).openSavedScreen();
    }

    @Test
    public void shouldDrawRoute() {
        final long routeId = 1;
        final ArrayList<LocationDbEntity> list = new ArrayList<>();
        list.add(new LocationDbEntity(routeId, 2.0f, 3.0f));
        when(repository.getRouteLocations(routeId)).thenReturn(Single.just(list));

        presenter.onCreate(routeId);

        verify(view).drawPath(list);
    }

    @Test
    public void shouldNotDrawRoute() {
        presenter.onCreate(NO_ROUTE_ID);

        verify(view, never()).drawPolyLine(any(LocationDbEntity.class));
    }
}