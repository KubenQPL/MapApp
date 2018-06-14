package pl.jakubneukirch.mapapp.data.model.api;

import com.google.gson.annotations.SerializedName;

public class DetailPhoto {
    private int height;
    private int width;
    @SerializedName("photo_reference") private String photoReference;

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }
}
