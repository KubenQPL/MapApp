package pl.jakubneukirch.mapapp.data.model.api;

import com.google.gson.annotations.SerializedName;

public class NearbyPlace {
    private String icon;
    private String id;
    private String name;
    @SerializedName("place_id") private String placeId;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
}
