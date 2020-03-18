package ie.adsfinder.adsfinder.api;


import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ElectronicsModel {

    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("results")
    @Expose
    private List<ElectronicResult> results = new ArrayList<ElectronicResult>();
    @SerializedName("count")
    @Expose
    private Integer count;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<ElectronicResult> getResults() {
        Log.d("from get Result function", results.toString());
        return results;
    }

    public void setResults(List<ElectronicResult> results) {
        this.results = results;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}