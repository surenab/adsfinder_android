package ie.adsfinder.adsfinder.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LightAddress {
    @SerializedName("county")
    @Expose
    private String county;
    @SerializedName("town")
    @Expose
    private String town;

    @SerializedName("longitude")
    @Expose
    private Float longitude;

    @SerializedName("latitude")
    @Expose
    private Float latitude;

    @Override
    public String toString() {
        return "LightAddress{" +
                "county='" + county + '\'' +
                ", town='" + town + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }
}
