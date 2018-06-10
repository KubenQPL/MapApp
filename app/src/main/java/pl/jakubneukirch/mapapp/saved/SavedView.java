package pl.jakubneukirch.mapapp.saved;

import java.util.List;

import pl.jakubneukirch.mapapp.base.MvpView;
import pl.jakubneukirch.mapapp.data.model.dto.RouteDto;

public interface SavedView extends MvpView {
    void setup();
    void setRoutes(List<RouteDto> routes);
}
