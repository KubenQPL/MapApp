package pl.jakubneukirch.mapapp.main;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import io.reactivex.Single;
import pl.jakubneukirch.mapapp.RxSchedulersOverrideRule;
import pl.jakubneukirch.mapapp.data.MapRepository;
import pl.jakubneukirch.mapapp.data.model.db.RouteLocationsDbEntity;
import pl.jakubneukirch.mapapp.saved.SavedPresenter;
import pl.jakubneukirch.mapapp.saved.SavedView;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static pl.jakubneukirch.mapapp.main.MainPresenter.*;

@RunWith(MockitoJUnitRunner.class)
public class SavedScreenTest {

    @Rule
    public RxSchedulersOverrideRule rxSchedulersOverrideRule = new RxSchedulersOverrideRule();
    @Mock SavedView view;
    @Mock MapRepository repository;
    @InjectMocks SavedPresenter presenter;

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
    public void shouldSetUpView() {
        when(repository.getAllRoutesWithLocations()).thenReturn(Single.just(new ArrayList<>()));

        presenter.onCreate();

        verify(view).setup();
    }

    @Test
    public void shouldSetRoutes() {
        final ArrayList<RouteLocationsDbEntity> list = new ArrayList<>();
        when(repository.getAllRoutesWithLocations()).thenReturn(Single.just(list));

        presenter.onCreate();

        verify(view).setRoutes(any());
    }

    @Test
    public void shouldGoBack() {
        presenter.onItemScreenSelected(MAIN_ACTIVITY_POSITION);

        verify(view).goBack();
    }

    @Test
    public void shouldDoNothing(){
        presenter.onItemScreenSelected(SAVED_ACTIVITY_POSITION);

        verify(view, never()).goBack();
    }
}
