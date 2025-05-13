package com.xtree.bet.bean.response.im;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.xtree.base.vo.BaseBean;

public class LiveStreamingUrl implements BaseBean, Parcelable {

    @SerializedName("Url")
    private String url;

    @SerializedName("Priority")
    private int priority;

    @SerializedName("Type")
    private int type;

    @SerializedName("Referrer")
    private String referrer;

    public LiveStreamingUrl() {
    }

    protected LiveStreamingUrl(Parcel in) {
        url = in.readString();
        priority = in.readInt();
        type = in.readInt();
        referrer = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeInt(priority);
        dest.writeInt(type);
        dest.writeString(referrer);
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

    // Getter and Setter methods
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    @Override
    public String toString() {
        return "LiveStreamingUrl{" +
                "url='" + url + '\'' +
                ", priority=" + priority +
                ", type=" + type +
                ", referrer='" + referrer + '\'' +
                '}';
    }
}
