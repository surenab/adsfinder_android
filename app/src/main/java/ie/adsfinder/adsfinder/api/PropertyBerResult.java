package ie.adsfinder.adsfinder.api;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PropertyBerResult {

    @SerializedName("ber_classification")
    @Expose
    private List<String> ber_classification;

    public List<String> getBer_classification() {
        return ber_classification;
    }

    public void setBer_classification(List<String> ber_classification) {
        this.ber_classification = ber_classification;
    }

    @Override
    public String toString() {
        return "PropertyBerResult{" +
                "ber_classification=" + ber_classification +
                '}';
    }
}