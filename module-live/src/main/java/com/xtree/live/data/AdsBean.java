package com.xtree.live.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AdsBean implements Serializable {

    @SerializedName("advertising_url")
    private String advertisingUrl;

    @SerializedName("ad_id")
    private String adId;

    @SerializedName("img_address")
    private String imgAddress;

    @SerializedName("id")
    private String id;

    @SerializedName("svga_url")
    private String svgaUrl;

    public String getAdvertisingUrl() {
        return advertisingUrl;
    }

    public void setAdvertisingUrl(String advertisingUrl) {
        this.advertisingUrl = advertisingUrl;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getImgAddress() {
        return imgAddress;
    }

    public void setImgAddress(String imgAddress) {
        this.imgAddress = imgAddress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSvgaUrl() {
        return svgaUrl;
    }

    public void setSvgaUrl(String svgaUrl) {
        this.svgaUrl = svgaUrl;
    }
}