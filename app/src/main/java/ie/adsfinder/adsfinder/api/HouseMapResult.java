package ie.adsfinder.adsfinder.api;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HouseMapResult {
    @SerializedName("address__latitude")
    @Expose
    private Float address__latitude;
    @SerializedName("address__longitude")
    @Expose
    private Float address__longitude;
    @SerializedName("id")
    @Expose
    private Integer id;

    public Float getAddress__latitude() {
        return address__latitude;
    }

    public void setAddress__latitude(Float address__latitude) {
        this.address__latitude = address__latitude;
    }

    public Float getAddress__longitude() {
        return address__longitude;
    }

    public void setAddress__longitude(Float address__longitude) {
        this.address__longitude = address__longitude;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}

