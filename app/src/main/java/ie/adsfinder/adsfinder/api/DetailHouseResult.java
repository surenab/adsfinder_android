package ie.adsfinder.adsfinder.api;


import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DetailHouseResult {
    @SerializedName("scrapedate")
    @Expose
    private String scrapedate;
    @SerializedName("lightaddress")
    @Expose
    private LightAddress lightaddress;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("donor")
    @Expose
    private String donor;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("mainimageurl")
    @Expose
    private String mainimageurl;
    @SerializedName("header")
    @Expose
    private String header;
    @SerializedName("video")
    @Expose
    private String video;
    @SerializedName("related_ads")
    @Expose
    private String related_ads;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("photos")
    @Expose
    private List<String> photos;



    @SerializedName("options")
    @Expose
    private JsonObject options;

    @SerializedName("generalinfo")
    @Expose
    private HouseGeneralResult generalinfo;

    @SerializedName("specifications")
    @Expose
    private JsonObject specifications;

    @SerializedName("contacts")
    @Expose
    private DetailContactResult contacts;

    public String getScrapedate() {
        return scrapedate;
    }

    public void setScrapedate(String scrapedate) {
        this.scrapedate = scrapedate;
    }

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getRelated_ads() {
        return related_ads;
    }

    public void setRelated_ads(String related_ads) {
        this.related_ads = related_ads;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public JsonObject getOptions() {
        return options;
    }

    public void setOptions(JsonObject options) {
        this.options = options;
    }

    public HouseGeneralResult getGeneralinfo() {
        return generalinfo;
    }

    public void setGeneralinfo(HouseGeneralResult generalinfo) {
        this.generalinfo = generalinfo;
    }

    public JsonObject getSpecifications() {
        return specifications;
    }

    public void setSpecifications(JsonObject specifications) {
        this.specifications = specifications;
    }

    public DetailContactResult getContacts() {
        return contacts;
    }

    public void setContacts(DetailContactResult contacts) {
        this.contacts = contacts;
    }

    @Override
    public String toString() {
        return "DetailResult{" +
                "scrapedate='" + scrapedate + '\'' +
                ", lightaddress=" + lightaddress +
                ", price=" + price +
                ", id=" + id +
                ", donor='" + donor + '\'' +
                ", url='" + url + '\'' +
                ", mainimageurl='" + mainimageurl + '\'' +
                ", header='" + header + '\'' +
                ", video='" + video + '\'' +
                ", related_ads='" + related_ads + '\'' +
                ", description='" + description + '\'' +
                ", photos=" + photos +
                ", options=" + options +
                ", generalinfo=" + generalinfo +
                ", specifications=" + specifications +
                ", contacts=" + contacts +
                '}';
    }
}

