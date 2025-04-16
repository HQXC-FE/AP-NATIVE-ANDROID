package com.xtree.bet.bean.response.im;

import android.os.Parcel;

import androidx.annotation.NonNull;

import com.xtree.base.vo.BaseBean;

import java.util.List;

/**
 * 获取所有比赛赛种
 */
public class SportCountRsp implements BaseBean {

    /**
     * 所有赛事信息
     */
    public List<SportMatch> sportCount;
    /**
     * 盘口id(对应hid)
     */
    public String serverTime;
    /**
     * 指出请求状态编码
     */
    public int statusCode;
    /**
     * 状态编码的描述含义.
     */
    public String statusDesc;


    public SportCountRsp() {
    }

    protected SportCountRsp(Parcel in) {
        this.sportCount = in.createTypedArrayList(SportMatch.CREATOR);
        this.serverTime = in.readString();
        this.statusCode = in.readInt();
        this.statusDesc = in.readString();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeTypedList(this.sportCount);
        dest.writeString(this.serverTime);
        dest.writeInt(this.statusCode);
        dest.writeString(this.statusDesc);
    }

    public void readFromParcel(Parcel source) {
        this.sportCount = source.createTypedArrayList(SportMatch.CREATOR);
        this.serverTime = source.readString();
        this.statusCode = source.readInt();
        this.statusDesc = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SportCountRsp> CREATOR = new Creator<SportCountRsp>() {
        @Override
        public SportCountRsp createFromParcel(Parcel source) {
            return new SportCountRsp(source);
        }

        @Override
        public SportCountRsp[] newArray(int size) {
            return new SportCountRsp[size];
        }
    };
}
