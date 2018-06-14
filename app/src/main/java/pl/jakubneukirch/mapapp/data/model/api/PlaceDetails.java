package pl.jakubneukirch.mapapp.data.model.api;

public class PlaceDetails {
    private DetailResult result = null;

    public static PlaceDetails empty() {
        return new PlaceDetails();
    }

    public DetailResult getResult() {
        return result;
    }

    public void setResult(DetailResult result) {
        this.result = result;
    }
}
