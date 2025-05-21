
package com.xtree.bet.bean.request.im;

import com.google.gson.annotations.SerializedName;
import com.xtree.bet.bean.request.im.ComboSelection;
import com.xtree.bet.bean.response.im.WagerSelectionInfo;

import java.util.List;

public class PlaceBetReq {

    @SerializedName("WagerType")
    private int wagerType;

    @SerializedName("CustomerIP")
    private String customerIP;

    @SerializedName("ServerIP")
    private String serverIP;

    @SerializedName("CustomerMACAddress")
    private String customerMACAddress;

    @SerializedName("UserAgent")
    private String userAgent;

    @SerializedName("IsComboAcceptAnyOdds")
    private boolean isComboAcceptAnyOdds;

    @SerializedName("WagerSelectionInfos")
    private List<WagerSelectionInfo> wagerSelectionInfos;

    @SerializedName("ComboSelections")
    private List<ComboSelection> comboSelections;

    @SerializedName("Token")
    private String token;
    @SerializedName("SourceWallet")
    private int sourceWallet;



    public int getWagerType() {
        return wagerType;
    }

    public void setWagerType(int wagerType) {
        this.wagerType = wagerType;
    }

    public String getCustomerIP() {
        return customerIP;
    }

    public void setCustomerIP(String customerIP) {
        this.customerIP = customerIP;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public String getCustomerMACAddress() {
        return customerMACAddress;
    }

    public void setCustomerMACAddress(String customerMACAddress) {
        this.customerMACAddress = customerMACAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public boolean isComboAcceptAnyOdds() {
        return isComboAcceptAnyOdds;
    }

    public void setComboAcceptAnyOdds(boolean comboAcceptAnyOdds) {
        isComboAcceptAnyOdds = comboAcceptAnyOdds;
    }

    public List<WagerSelectionInfo> getWagerSelectionInfos() {
        return wagerSelectionInfos;
    }

    public void setWagerSelectionInfos(List<WagerSelectionInfo> wagerSelectionInfos) {
        this.wagerSelectionInfos = wagerSelectionInfos;
    }

    public List<ComboSelection> getComboSelections() {
        return comboSelections;
    }

    public void setComboSelections(List<ComboSelection> comboSelections) {
        this.comboSelections = comboSelections;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getSourceWallet() {
        return sourceWallet;
    }

    public void setSourceWallet(int sourceWallet) {
        this.sourceWallet = sourceWallet;
    }

}
