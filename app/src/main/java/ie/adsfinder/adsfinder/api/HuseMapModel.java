package ie.adsfinder.adsfinder.api;


import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class HuseMapModel {

    @SerializedName("results")
    @Expose
    private List<HouseMapResult> results = new ArrayList<HouseMapResult>();

    public List<HouseMapResult> getResults() {
        return results;
    }

    public void setResults(List<HouseMapResult> results) {
        this.results = results;
    }

}