package com.xtree.bet.bean.response.im;

import android.os.Parcel;

import androidx.annotation.NonNull;

import com.xtree.base.vo.BaseBean;

import java.util.List;

public class SportEventRsp implements BaseBean {

    public List<SportMatch> sports;
    public String serverTime;
    public int statusCode;
    public String statusDesc;

    public SportEventRsp() {}

    protected SportEventRsp(Parcel in) {
        this.sports = in.createTypedArrayList(SportMatch.CREATOR);
        this.serverTime = in.readString();
        this.statusCode = in.readInt();
        this.statusDesc = in.readString();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeTypedList(this.sports);
        dest.writeString(this.serverTime);
        dest.writeInt(this.statusCode);
        dest.writeString(this.statusDesc);
    }

    public void readFromParcel(Parcel source) {
        this.sports = source.createTypedArrayList(SportMatch.CREATOR);
        this.serverTime = source.readString();
        this.statusCode = source.readInt();
        this.statusDesc = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SportEventRsp> CREATOR = new Creator<SportEventRsp>() {
        @Override
        public SportEventRsp createFromParcel(Parcel source) {
            return new SportEventRsp(source);
        }

        @Override
        public SportEventRsp[] newArray(int size) {
            return new SportEventRsp[size];
        }
    };
} // Sport 类以及其下所有嵌套类 Event, MarketLine, WagerSelection 等接下来会继续添加。
