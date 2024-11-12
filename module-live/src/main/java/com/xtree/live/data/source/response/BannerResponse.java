package com.xtree.live.data.source.response;

public class BannerResponse {
    private int id;
    private String title;
    private int type;//类型(对应后台设置的轮播图分类ID)
    private String describe;
    private String url;//链接地址
    private String iosUrl;//IOS链接地址
    private String androidUrl;//安卓链接地址
    private String paramType;//比赛类型(赛事分类)
    private int paramId;//比赛ID(赛事id)
    private int anchorId;
    private String img;
    private String foreImg;
    private String backImg;
    private int linkType;//链接类型(1:直播中的比赛,2:正在进行的比赛)
    private String params;
    private int listOrder;
    private int status;//状态(0:取消,1:正常)
    private int updatetime;
    private int addtime;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIosUrl() {
        return iosUrl;
    }

    public void setIosUrl(String iosUrl) {
        this.iosUrl = iosUrl;
    }

    public String getAndroidUrl() {
        return androidUrl;
    }

    public void setAndroidUrl(String androidUrl) {
        this.androidUrl = androidUrl;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public int getParamId() {
        return paramId;
    }

    public void setParamId(int paramId) {
        this.paramId = paramId;
    }

    public int getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(int anchorId) {
        this.anchorId = anchorId;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getForeImg() {
        return foreImg;
    }

    public void setForeImg(String foreImg) {
        this.foreImg = foreImg;
    }

    public String getBackImg() {
        return backImg;
    }

    public void setBackImg(String backImg) {
        this.backImg = backImg;
    }

    public int getLinkType() {
        return linkType;
    }

    public void setLinkType(int linkType) {
        this.linkType = linkType;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public int getListOrder() {
        return listOrder;
    }

    public void setListOrder(int listOrder) {
        this.listOrder = listOrder;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(int updatetime) {
        this.updatetime = updatetime;
    }

    public int getAddtime() {
        return addtime;
    }

    public void setAddtime(int addtime) {
        this.addtime = addtime;
    }
}

