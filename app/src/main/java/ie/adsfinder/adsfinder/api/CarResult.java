package ie.adsfinder.adsfinder.api;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CarResult {
    public LightAddress getLightaddress() {
        return lightaddress;
    }

    public void setLightaddress(LightAddress lightaddress) {
        this.lightaddress = lightaddress;
    }

    @SerializedName("scrapedate")
    @Expose
    private String scrapedate;
    @SerializedName("lightaddress")
    @Expose
    private LightAddress lightaddress;

    public String getScrapedate() {
        return scrapedate;
    }

    public void setScrapedate(String scrapedate) {
        this.scrapedate = scrapedate;
    }

    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("firstregyear")
    @Expose
    private Integer firstregyear;
    @SerializedName("kilometer")
    @Expose
    private Integer kilometer;
    @SerializedName("fueltype")
    @Expose
    private String fueltype;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("transmission")
    @Expose
    private String transmission;
    @SerializedName("donorwebsite")
    @Expose
    private String donorwebsite;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("mainimageurl")
    @Expose
    private String mainimageurl;
    @SerializedName("header")
    @Expose
    private String header;

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getFirstregyear() {
        return firstregyear;
    }

    public void setFirstregyear(Integer firstregyear) {
        this.firstregyear = firstregyear;
    }

    public Integer getKilometer() {
        return kilometer;
    }

    public void setKilometer(Integer kilometer) {
        this.kilometer = kilometer;
    }

    public String getFueltype() {
        return fueltype;
    }

    public void setFueltype(String fueltype) {
        this.fueltype = fueltype;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public String getDonorwebsite() {
        return donorwebsite;
    }

    public void setDonorwebsite(String donorwebsite) {
        this.donorwebsite = donorwebsite;
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

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}

