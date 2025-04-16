package com.xtree.bet.bean.response.im;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

public class LiveStreamingUrl implements BaseBean {

    public String Url;
    public int Priority;
    public int Type;
    public String Referrer;

    protected LiveStreamingUrl(Parcel in) {
        Url = in.readString();
        Priority = in.readInt();
        Type = in.readInt();
        Referrer = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Url);
        dest.writeInt(Priority);
        dest.writeInt(Type);
        dest.writeString(Referrer);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LiveStreamingUrl> CREATOR = new Creator<LiveStreamingUrl>() {
        @Override
        public LiveStreamingUrl createFromParcel(Parcel in) {
            return new LiveStreamingUrl(in);
        }

        @Override
        public LiveStreamingUrl[] newArray(int size) {
            return new LiveStreamingUrl[size];
        }
    };
}
