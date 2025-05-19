package com.xtree.bet.bean.request.im;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.xtree.bet.bean.response.im.WagerSelectionInfo;

import java.util.List;

public class GetBetInfoReq  {

    /**
     * 指出投注性质.
     * 1 = Single单注
     * 2 = Combo连串过关
     */
    private int WagerType;

    private List<WagerSelectionInfo> WagerSelectionInfos;


    /**
     * 后台统一处理
     */
    private String Token;


    public GetBetInfoReq() {}


    public int getWagerType() {
        return WagerType;
    }

    public void setWagerType(int WagerType) {
        this.WagerType = WagerType;
    }

    public List<WagerSelectionInfo> getWagerSelectionInfos() {
        return WagerSelectionInfos;
    }

    public void setWagerSelectionInfos(List<WagerSelectionInfo> WagerSelectionInfos) {
        this.WagerSelectionInfos = WagerSelectionInfos;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String Token) {
        this.Token = Token;
    }

}
