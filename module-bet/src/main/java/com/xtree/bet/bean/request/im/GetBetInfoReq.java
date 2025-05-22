package com.xtree.bet.bean.request.im;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.xtree.bet.bean.response.im.WagerSelectionInfo;

import java.util.List;

public class GetBetInfoReq  extends BaseIMRequest{

    /**
     * 指出投注性质.
     * 1 = Single单注
     * 2 = Combo连串过关
     */
    private int WagerType;

    private List<WagerSelectionInfo> WagerSelectionInfos;


    public GetBetInfoReq(int wagerType, List<WagerSelectionInfo> wagerSelectionInfos, String token) {
        WagerType = wagerType;
        WagerSelectionInfos = wagerSelectionInfos;
    }

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

}
