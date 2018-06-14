package pl.jakubneukirch.mapapp.data.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DetailResult {
    @SerializedName("formatted_address") private String address;
    private String name;
    private List<DetailPhoto> photos;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DetailPhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<DetailPhoto> photos) {
        this.photos = photos;
    }
}
