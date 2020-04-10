package ie.adsfinder.adsfinder.api;


import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CarForm {

    @SerializedName("fueltype")
    @Expose
    private List<String> fueltype;
    @SerializedName("transmission")
    @Expose
    private List<String> transmission;
    @SerializedName("condition")
    @Expose
    private List<String> condition;
    @SerializedName("makemodel")
    @Expose
    private JsonObject makemodel;

    @Override
    public String toString() {
        return "CarForm{" +
                "fueltype=" + fueltype +
                ", transmission=" + transmission +
                ", condition=" + condition +
                ", makemodel=" + makemodel +
                '}';
    }

    public List<String> getFueltype() {
        return fueltype;
    }

    public void setFueltype(List<String> fueltype) {
        this.fueltype = fueltype;
    }

    public List<String> getTransmission() {
        return transmission;
    }

    public void setTransmission(List<String> transmission) {
        this.transmission = transmission;
    }

    public List<String> getCondition() {
        return condition;
    }

    public void setCondition(List<String> condition) {
        this.condition = condition;
    }

    public JsonObject getMakemodel() {
        return makemodel;
    }

    public void setMakemodel(JsonObject makemodel) {
        this.makemodel = makemodel;
    }
}