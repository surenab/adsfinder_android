package ie.adsfinder.adsfinder.api;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MakeModelIDModel {

    @SerializedName("make__name")
    @Expose
    private List<ModelIDModel> make__name;

    public List<ModelIDModel> getMake__name() {
        return make__name;
    }

    public void setMake__name(List<ModelIDModel> make__name) {
        this.make__name = make__name;
    }

    @Override
    public String toString() {
        return "MakeModelIDModel{" +
                "make__name=" + make__name +
                '}';
    }
}