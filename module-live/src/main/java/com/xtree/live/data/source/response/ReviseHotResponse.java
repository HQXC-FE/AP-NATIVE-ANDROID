package com.xtree.live.data.source.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by KAKA on 2024/10/25.
 * Describe: 直播配置文件返回体
 */
public class ReviseHotResponse {

    //{"activity_game":0,"androidDownloadText":"\u6d4b\u8bd5","androidDownloadUrl":"http:\/\/www.adroid.com\/aaa-AAA.apk1","androidMandatoryUpdateSandbox":"0","androidVersionMumber":"1.3.6  213","channel_updapp":0,"channelCode":"xt","chat_msg_close":0,"chat_sysinvite":1,"chat_system":"ws","CountryCode":[],"day":0,"domain":"zhibo-apps.oxldkm.com","ip":"27.110.152.130","is_redbag":[],"news":1,"prefix":"xt","register_way":"2","report":0,"share_domain":"http:\/\/b2b.93.back.com\/","site_gwa":"\u516c\u7f51\u5b89\u5907000000","site_icp":"\u743cICP\u590700000\u53f7","site_referer":"","textLinkUrl":"","uid":22,"video_ad_bar_content":"video-ad-bar-content","video_ad_bar_title":"TIPS","replace_video_ad_bar_channels_code":["default"],"replace_video_ad_bar_title":"","replace_video_ad_bar_content":"","startScreenUrl":"","loadingCoverScreenUrl":"","loadingCoverScreenLink":"","videoStopAdvertisingImageUrl":"","videoStopAdvertisingImageLink":""}

    /**
     * activityGame
     */
    @SerializedName("activity_game")
    private int activityGame;
    /**
     * androidDownloadText
     */
    @SerializedName("androidDownloadText")
    private String androidDownloadText;
    /**
     * androidDownloadUrl
     */
    @SerializedName("androidDownloadUrl")
    private String androidDownloadUrl;
    /**
     * androidMandatoryUpdateSandbox
     */
    @SerializedName("androidMandatoryUpdateSandbox")
    private String androidMandatoryUpdateSandbox;
    /**
     * androidVersionMumber
     */
    @SerializedName("androidVersionMumber")
    private String androidVersionMumber;
    /**
     * channelUpdapp
     */
    @SerializedName("channel_updapp")
    private int channelUpdapp;
    /**
     * channelCode
     */
    @SerializedName("channelCode")
    private String channelCode;
    /**
     * chatMsgClose
     */
    @SerializedName("chat_msg_close")
    private int chatMsgClose;
    /**
     * chatSysinvite
     */
    @SerializedName("chat_sysinvite")
    private int chatSysinvite;
    /**
     * chatSystem
     */
    @SerializedName("chat_system")
    private String chatSystem;
    /**
     * countryCode
     */
    @SerializedName("CountryCode")
    private List<?> countryCode;
    /**
     * day
     */
    @SerializedName("day")
    private int day;
    /**
     * domain
     */
    @SerializedName("domain")
    private String domain;
    /**
     * ip
     */
    @SerializedName("ip")
    private String ip;
    /**
     * isRedbag
     */
    @SerializedName("is_redbag")
    private List<?> isRedbag;
    /**
     * news
     */
    @SerializedName("news")
    private int news;
    /**
     * prefix
     */
    @SerializedName("prefix")
    private String prefix;
    /**
     * registerWay
     */
    @SerializedName("register_way")
    private String registerWay;
    /**
     * report
     */
    @SerializedName("report")
    private int report;
    /**
     * shareDomain
     */
    @SerializedName("share_domain")
    private String shareDomain;
    /**
     * siteGwa
     */
    @SerializedName("site_gwa")
    private String siteGwa;
    /**
     * siteIcp
     */
    @SerializedName("site_icp")
    private String siteIcp;
    /**
     * siteReferer
     */
    @SerializedName("site_referer")
    private String siteReferer;
    /**
     * textLinkUrl
     */
    @SerializedName("textLinkUrl")
    private String textLinkUrl;
    /**
     * uid
     */
    @SerializedName("uid")
    private int uid;
    /**
     * videoAdBarContent
     */
    @SerializedName("video_ad_bar_content")
    private String videoAdBarContent;
    /**
     * videoAdBarTitle
     */
    @SerializedName("video_ad_bar_title")
    private String videoAdBarTitle;
    /**
     * replaceVideoAdBarChannelsCode
     */
    @SerializedName("replace_video_ad_bar_channels_code")
    private List<String> replaceVideoAdBarChannelsCode;
    /**
     * replaceVideoAdBarTitle
     */
    @SerializedName("replace_video_ad_bar_title")
    private String replaceVideoAdBarTitle;
    /**
     * replaceVideoAdBarContent
     */
    @SerializedName("replace_video_ad_bar_content")
    private String replaceVideoAdBarContent;
    /**
     * startScreenUrl
     */
    @SerializedName("startScreenUrl")
    private String startScreenUrl;
    /**
     * loadingCoverScreenUrl
     */
    @SerializedName("loadingCoverScreenUrl")
    private String loadingCoverScreenUrl;
    /**
     * loadingCoverScreenLink
     */
    @SerializedName("loadingCoverScreenLink")
    private String loadingCoverScreenLink;
    /**
     * videoStopAdvertisingImageUrl
     */
    @SerializedName("videoStopAdvertisingImageUrl")
    private String videoStopAdvertisingImageUrl;
    /**
     * videoStopAdvertisingImageLink
     */
    @SerializedName("videoStopAdvertisingImageLink")
    private String videoStopAdvertisingImageLink;

    public int getActivityGame() {
        return activityGame;
    }

    public void setActivityGame(int activityGame) {
        this.activityGame = activityGame;
    }

    public String getAndroidDownloadText() {
        return androidDownloadText;
    }

    public void setAndroidDownloadText(String androidDownloadText) {
        this.androidDownloadText = androidDownloadText;
    }

    public String getAndroidDownloadUrl() {
        return androidDownloadUrl;
    }

    public void setAndroidDownloadUrl(String androidDownloadUrl) {
        this.androidDownloadUrl = androidDownloadUrl;
    }

    public String getAndroidMandatoryUpdateSandbox() {
        return androidMandatoryUpdateSandbox;
    }

    public void setAndroidMandatoryUpdateSandbox(String androidMandatoryUpdateSandbox) {
        this.androidMandatoryUpdateSandbox = androidMandatoryUpdateSandbox;
    }

    public String getAndroidVersionMumber() {
        return androidVersionMumber;
    }

    public void setAndroidVersionMumber(String androidVersionMumber) {
        this.androidVersionMumber = androidVersionMumber;
    }

    public int getChannelUpdapp() {
        return channelUpdapp;
    }

    public void setChannelUpdapp(int channelUpdapp) {
        this.channelUpdapp = channelUpdapp;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public int getChatMsgClose() {
        return chatMsgClose;
    }

    public void setChatMsgClose(int chatMsgClose) {
        this.chatMsgClose = chatMsgClose;
    }

    public int getChatSysinvite() {
        return chatSysinvite;
    }

    public void setChatSysinvite(int chatSysinvite) {
        this.chatSysinvite = chatSysinvite;
    }

    public String getChatSystem() {
        return chatSystem;
    }

    public void setChatSystem(String chatSystem) {
        this.chatSystem = chatSystem;
    }

    public List<?> getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(List<?> countryCode) {
        this.countryCode = countryCode;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public List<?> getIsRedbag() {
        return isRedbag;
    }

    public void setIsRedbag(List<?> isRedbag) {
        this.isRedbag = isRedbag;
    }

    public int getNews() {
        return news;
    }

    public void setNews(int news) {
        this.news = news;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getRegisterWay() {
        return registerWay;
    }

    public void setRegisterWay(String registerWay) {
        this.registerWay = registerWay;
    }

    public int getReport() {
        return report;
    }

    public void setReport(int report) {
        this.report = report;
    }

    public String getShareDomain() {
        return shareDomain;
    }

    public void setShareDomain(String shareDomain) {
        this.shareDomain = shareDomain;
    }

    public String getSiteGwa() {
        return siteGwa;
    }

    public void setSiteGwa(String siteGwa) {
        this.siteGwa = siteGwa;
    }

    public String getSiteIcp() {
        return siteIcp;
    }

    public void setSiteIcp(String siteIcp) {
        this.siteIcp = siteIcp;
    }

    public String getSiteReferer() {
        return siteReferer;
    }

    public void setSiteReferer(String siteReferer) {
        this.siteReferer = siteReferer;
    }

    public String getTextLinkUrl() {
        return textLinkUrl;
    }

    public void setTextLinkUrl(String textLinkUrl) {
        this.textLinkUrl = textLinkUrl;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getVideoAdBarContent() {
        return videoAdBarContent;
    }

    public void setVideoAdBarContent(String videoAdBarContent) {
        this.videoAdBarContent = videoAdBarContent;
    }

    public String getVideoAdBarTitle() {
        return videoAdBarTitle;
    }

    public void setVideoAdBarTitle(String videoAdBarTitle) {
        this.videoAdBarTitle = videoAdBarTitle;
    }

    public List<String> getReplaceVideoAdBarChannelsCode() {
        return replaceVideoAdBarChannelsCode;
    }

    public void setReplaceVideoAdBarChannelsCode(List<String> replaceVideoAdBarChannelsCode) {
        this.replaceVideoAdBarChannelsCode = replaceVideoAdBarChannelsCode;
    }

    public String getReplaceVideoAdBarTitle() {
        return replaceVideoAdBarTitle;
    }

    public void setReplaceVideoAdBarTitle(String replaceVideoAdBarTitle) {
        this.replaceVideoAdBarTitle = replaceVideoAdBarTitle;
    }

    public String getReplaceVideoAdBarContent() {
        return replaceVideoAdBarContent;
    }

    public void setReplaceVideoAdBarContent(String replaceVideoAdBarContent) {
        this.replaceVideoAdBarContent = replaceVideoAdBarContent;
    }

    public String getStartScreenUrl() {
        return startScreenUrl;
    }

    public void setStartScreenUrl(String startScreenUrl) {
        this.startScreenUrl = startScreenUrl;
    }

    public String getLoadingCoverScreenUrl() {
        return loadingCoverScreenUrl;
    }

    public void setLoadingCoverScreenUrl(String loadingCoverScreenUrl) {
        this.loadingCoverScreenUrl = loadingCoverScreenUrl;
    }

    public String getLoadingCoverScreenLink() {
        return loadingCoverScreenLink;
    }

    public void setLoadingCoverScreenLink(String loadingCoverScreenLink) {
        this.loadingCoverScreenLink = loadingCoverScreenLink;
    }

    public String getVideoStopAdvertisingImageUrl() {
        return videoStopAdvertisingImageUrl;
    }

    public void setVideoStopAdvertisingImageUrl(String videoStopAdvertisingImageUrl) {
        this.videoStopAdvertisingImageUrl = videoStopAdvertisingImageUrl;
    }

    public String getVideoStopAdvertisingImageLink() {
        return videoStopAdvertisingImageLink;
    }

    public void setVideoStopAdvertisingImageLink(String videoStopAdvertisingImageLink) {
        this.videoStopAdvertisingImageLink = videoStopAdvertisingImageLink;
    }
}
