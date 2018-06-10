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

public class SavedPresenter extends BasePresenter<SavedView> {

    private MapRepository repository;

    @Inject
    public SavedPresenter(MapRepository repository) {
        this.repository = repository;
    }

    void onCreate() {
        view.setup();
        loadRoutes();
    }

    private void loadRoutes() {
        disposables.add(repository.getAllRoutesWithLocations()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess((List<RouteLocationsDbEntity> list) -> {
                    view.setRoutes(mapAllRoutes(list));
                })
                .subscribe()
        );
    }

    private List<RouteDto> mapAllRoutes(List<RouteLocationsDbEntity> routes) {
        final ArrayList<RouteDto> outRoutes = new ArrayList<>();
        for (RouteLocationsDbEntity route : routes) {
            outRoutes.add(RouteDto.mapRouteDto(route, MapsStaticApiUtils.getPathUrl(route.getLocations())));
        }
        return outRoutes;
    }
}
