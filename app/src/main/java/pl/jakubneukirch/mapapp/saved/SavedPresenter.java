package pl.jakubneukirch.mapapp.saved;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import pl.jakubneukirch.mapapp.base.BasePresenter;
import pl.jakubneukirch.mapapp.common.MapsStaticApiUtils;
import pl.jakubneukirch.mapapp.data.MapRepository;
import pl.jakubneukirch.mapapp.data.model.db.RouteLocationsDbEntity;
import pl.jakubneukirch.mapapp.data.model.dto.RouteDto;

import static pl.jakubneukirch.mapapp.main.MainPresenter.MAIN_ACTIVITY_POSITION;

public class SavedPresenter extends BasePresenter<SavedView> {

    private MapRepository repository;
    private List<RouteDto> routes = null;

    @Inject
    public SavedPresenter(MapRepository repository) {
        this.repository = repository;
    }

    public void onCreate() {
        view.setup();
        loadRoutes();
    }

    public void itemScreenSelected(int position) {
        if (position == MAIN_ACTIVITY_POSITION) {
            view.goBack();
        }
    }

    private void loadRoutes() {
        disposables.add(repository.getAllRoutesWithLocations()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess((List<RouteLocationsDbEntity> list) -> setupRoutes(mapAllRoutes(list)))
                .subscribe()
        );
    }

    private void setupRoutes(List<RouteDto> list) {
        this.routes = list;
        view.setRoutes(list);
    }

    public void routeSelected(int position) {
        view.openRoute(routes.get(position).getId());
    }

    private List<RouteDto> mapAllRoutes(List<RouteLocationsDbEntity> routes) {
        final ArrayList<RouteDto> outRoutes = new ArrayList<>();
        for (RouteLocationsDbEntity route : routes) {
            outRoutes.add(RouteDto.mapRouteDto(route, MapsStaticApiUtils.getPathUrl(route.getLocations())));
        }
        return outRoutes;
    }
}