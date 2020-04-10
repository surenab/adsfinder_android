package ie.adsfinder.adsfinder.api;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HouseResult {
    @SerializedName("lightaddress")
    @Expose
    private LightAddress lightaddress;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("ber_classification")
    @Expose
    private String ber_classification;
    @SerializedName("property_type")
    @Expose
    private String property_type;
    @SerializedName("property_category")
    @Expose
    private String property_category;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("property_title")
    @Expose
    private String property_title;
    @SerializedName("donor")
    @Expose
    private String donor;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("mainimageurl")
    @Expose
    private String mainimageurl;
    @SerializedName("beds")
    @Expose
    private Integer beds;
    @SerializedName("bathrooms")
    @Expose
    private Integer bathrooms;
    @SerializedName("scrapedate")
    @Expose
    private String scrapedate;

    public LightAddress getAddress() {
        return lightaddress;
    }

    public void setAddress(LightAddress lightaddress) {
        this.lightaddress = lightaddress;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getBer_classification() {
        return ber_classification;
    }

    public void setBer_classification(String ber_classification) {
        this.ber_classification = ber_classification;
    }

    public String getProperty_type() {
        return property_type;
    }

    public void setProperty_type(String property_type) {
        this.property_type = property_type;
    }

    public String getProperty_category() {
        return property_category;
    }

    public void setProperty_category(String property_category) {
        this.property_category = property_category;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProperty_title() {
        return property_title;
    }

    public void setProperty_title(String property_title) {
        this.property_title = property_title;
    }

    public String getDonor() {
        return donor;
    }

    public void setDonor(String donor) {
        this.donor = donor;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMainimageurl() {
        return mainimageurl;
    }

    public void setMainimageurl(String mainimageurl) {
        this.mainimageurl = mainimageurl;
    }

    public Integer getBeds() {
        return beds;
    }

    public void setBeds(Integer beds) {
        this.beds = beds;
    }

    public Integer getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(Integer bathrooms) {
        this.bathrooms = bathrooms;
    }

    public String getScrapedate() {
        return scrapedate;
    }

    public void setScrapedate(String scrapedate) {
        this.scrapedate = scrapedate;
    }
}

