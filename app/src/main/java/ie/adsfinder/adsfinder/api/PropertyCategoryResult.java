package ie.adsfinder.adsfinder.api;


import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PropertyCategoryResult {

    @SerializedName("property_category")
    @Expose
    private List<String> property_category;

    @Override
    public String toString() {
        return "PropertyCategoryResult{" +
                "property_category=" + property_category +
                '}';
    }

    public List<String> getProperty_category() {
        return property_category;
    }

    public void setProperty_category(List<String> property_category) {
        this.property_category = property_category;
    }
}