package ie.adsfinder.adsfinder.api;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ElectronicResult {
    @SerializedName("lightaddress")
    @Expose
    private LightAddress lightaddress;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("scrapedate")
    @Expose
    private String scrapedate;
    @SerializedName("header")
    @Expose
    private String header;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("mainimageurl")
    @Expose
    private String mainimageurl;
    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("donor")
    @Expose
    private String donor;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("electronic_category")
    @Expose
    private ElectronicCategory electronic_category;

    public LightAddress getLightaddress() {
        return lightaddress;
    }

    public void setLightaddress(LightAddress lightaddress) {
        this.lightaddress = lightaddress;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getScrapedate() {
        return scrapedate;
    }

    public void setScrapedate(String scrapedate) {
        this.scrapedate = scrapedate;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMainimageurl() {
        return mainimageurl;
    }

    public void setMainimageurl(String mainimageurl) {
        this.mainimageurl = mainimageurl;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDonor() {
        return donor;
    }

    public void setDonor(String donor) {
        this.donor = donor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ElectronicCategory getElectronic_category() {
        return electronic_category;
    }

    public void setElectronic_category(ElectronicCategory electronic_category) {
        this.electronic_category = electronic_category;
    }
}

