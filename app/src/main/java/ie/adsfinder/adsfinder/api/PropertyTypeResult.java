package ie.adsfinder.adsfinder.api;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PropertyTypeResult {

    @SerializedName("property_types")
    @Expose
    private List<String> property_types;

    public List<String> getProperty_types() {
        return property_types;
    }

    public void setProperty_types(List<String> property_types) {
        this.property_types = property_types;
    }

    @Override
    public String toString() {
        return "PropertyTypeResult{" +
                "property_types=" + property_types +
                '}';
    }
}