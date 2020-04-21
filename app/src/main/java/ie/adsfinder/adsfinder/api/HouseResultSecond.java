package ie.adsfinder.adsfinder.api;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HouseResultSecond {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("mainimageurl")
    @Expose
    private String mainimageurl;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("lightaddress")
    @Expose
    private LightAddress lightaddress;
    @SerializedName("video")
    @Expose
    private String  video;
    @SerializedName("donor")
    @Expose
    private String donor;
    @SerializedName("header")
    @Expose
    private String header;

    @SerializedName("photos")
    @Expose
    private List<String> photos;
    @SerializedName("generalinfo")
    @Expose
    private HouseGeneralResult generalinfo;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public LightAddress getLightaddress() {
        return lightaddress;
    }

    public void setLightaddress(LightAddress lightaddress) {
        this.lightaddress = lightaddress;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getDonor() {
        return donor;
    }

    public void setDonor(String donor) {
        this.donor = donor;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public HouseGeneralResult getGeneralinfo() {
        return generalinfo;
    }

    public void setGeneralinfo(HouseGeneralResult generalinfo) {
        this.generalinfo = generalinfo;
    }

    @Override
    public String toString() {
        return "HouseResult{" +
                "id=" + id +
                ", mainimageurl='" + mainimageurl + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", price=" + price +
                ", lightaddress=" + lightaddress +
                ", video='" + video + '\'' +
                ", donor='" + donor + '\'' +
                ", header='" + header + '\'' +
                ", photos=" + photos +
                ", generalinfo=" + generalinfo +
                '}';
    }
}

