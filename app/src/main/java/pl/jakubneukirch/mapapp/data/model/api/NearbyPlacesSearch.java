package pl.jakubneukirch.mapapp.data.model.api;

import java.util.List;

public class NearbyPlacesSearch {
    private List<NearbyPlace> results;

    public List<NearbyPlace> getResults() {
        return results;
    }

    public void setResults(List<NearbyPlace> results) {
        this.results = results;
    }
}
