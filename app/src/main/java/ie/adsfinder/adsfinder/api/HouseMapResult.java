package ie.adsfinder.adsfinder.api;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HouseMapResult {
    @SerializedName("lightaddress__latitude")
    @Expose
    private Float lightaddress__latitude;
    @SerializedName("lightaddress__longitude")
    @Expose
    private Float lightaddress__longitude;
    @SerializedName("id")
    @Expose
    private Integer id;

    public Float getAddress__latitude() {
        return lightaddress__latitude;
    }

    public void setAddress__latitude(Float lightaddress__latitude) {
        this.lightaddress__latitude = lightaddress__latitude;
    }

    public Float getAddress__longitude() {
        return lightaddress__longitude;
    }

    public void setAddress__longitude(Float lightaddress__longitude) {
        this.lightaddress__longitude = lightaddress__longitude;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}

