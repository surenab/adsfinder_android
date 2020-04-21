package ie.adsfinder.adsfinder.api;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HouseGeneralResult {
    @SerializedName("facility")
    @Expose
    private String facility;
    @SerializedName("surface")
    @Expose
    private String surface;
    @SerializedName("donor_logo")
    @Expose
    private String donor_logo;
    @SerializedName("seller type")
    @Expose
    private String seller_type;
    @SerializedName("property type")
    @Expose
    private String property_type;
    @SerializedName("scrap date")
    @Expose
    private String scrap_date;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("ber classification")
    @Expose
    private String ber_classification;
    @SerializedName("seller name")
    @Expose
    private String seller_name;
    @SerializedName("baths")
    @Expose
    private Integer baths;
    @SerializedName("beds")
    @Expose
    private Integer beds;
    @SerializedName("area")
    @Expose
    private String  area;
    @SerializedName("county")
    @Expose
    private String county;

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public String getSurface() {
        return surface;
    }

    public void setSurface(String surface) {
        this.surface = surface;
    }

    public String getDonor_logo() {
        return donor_logo;
    }

    public void setDonor_logo(String donor_logo) {
        this.donor_logo = donor_logo;
    }

    public String getSeller_type() {
        return seller_type;
    }

    public void setSeller_type(String seller_type) {
        this.seller_type = seller_type;
    }

    public String getProperty_type() {
        return property_type;
    }

    public void setProperty_type(String property_type) {
        this.property_type = property_type;
    }

    public String getScrap_date() {
        return scrap_date;
    }

    public void setScrap_date(String scrap_date) {
        this.scrap_date = scrap_date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBer_classification() {
        return ber_classification;
    }

    public void setBer_classification(String ber_classification) {
        this.ber_classification = ber_classification;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public Integer getBaths() {
        return baths;
    }

    public void setBaths(Integer baths) {
        this.baths = baths;
    }

    public Integer getBeds() {
        return beds;
    }

    public void setBeds(Integer beds) {
        this.beds = beds;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    @Override
    public String toString() {
        return "HouseGeneralResult{" +
                "facility='" + facility + '\'' +
                ", surface='" + surface + '\'' +
                ", donor_logo='" + donor_logo + '\'' +
                ", seller_type='" + seller_type + '\'' +
                ", property_type='" + property_type + '\'' +
                ", scrap_date='" + scrap_date + '\'' +
                ", category='" + category + '\'' +
                ", ber_classification='" + ber_classification + '\'' +
                ", seller_name='" + seller_name + '\'' +
                ", baths=" + baths +
                ", beds=" + beds +
                ", area='" + area + '\'' +
                ", county='" + county + '\'' +
                '}';
    }
}

