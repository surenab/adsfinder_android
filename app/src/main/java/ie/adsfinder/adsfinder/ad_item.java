package ie.adsfinder.adsfinder;

public class ad_item {
    String header;
    String donor;
    String scrapdate;
    String county;
    String param1;
    String param2;
    String extradata;
    int price;
    String imageUrl;
    String url;

    public void setHeader(String header) {
        this.header = header;
    }

    public void setDonor(String donor) {
        this.donor = donor;
    }

    public void setScrapdate(String scrapdate) {
        this.scrapdate = scrapdate;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    public void setExtradata(String extradata) {
        this.extradata = extradata;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getHeader() {
        return header;
    }

    public String getDonor() {
        return donor;
    }

    public String getScrapdate() {
        return scrapdate;
    }

    public String getCounty() {
        return county;
    }

    public String getParam1() {
        return param1;
    }

    public String getParam2() {
        return param2;
    }

    public String getExtradata() {
        return extradata;
    }

    public int getPrice() {
        return price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {

        return imageUrl;
    }

    public ad_item(String header, String donor, String scrapdate, String county, String param1, String param2, String extradata, int price, String imageUrl, String url) {
        this.header = header;
        this.donor = donor;
        this.scrapdate = scrapdate;
        this.county = county;
        this.param1 = param1;
        this.param2 = param2;
        this.extradata = extradata;
        this.price = price;
        this.imageUrl = imageUrl;
        this.url = url;
    }
}
