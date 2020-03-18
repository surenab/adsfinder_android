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
