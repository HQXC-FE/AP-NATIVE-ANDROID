package com.xtree.bet.bean.response.im;

import android.os.Parcel;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.xtree.base.vo.BaseBean;

import java.util.Date;
import java.util.List;

import android.os.Parcelable;

public class EventInfoByPageListRsp implements BaseBean, Parcelable {
    @SerializedName("Sports")
    private List<Sport> sports;
    @SerializedName("Page")
    private int page;
    @SerializedName("PageSize")
    private int pageSize;
    @SerializedName("TempDelta")
    private String tempDelta;
    @SerializedName("Delta")
    private long delta;
    @SerializedName("ServerTime")
    private Date serverTime;
    @SerializedName("StatusCode")
    private int statusCode;
    @SerializedName("StatusDesc")
    private String statusDesc;

    public EventInfoByPageListRsp() {
    }

    protected EventInfoByPageListRsp(Parcel in) {
        sports = in.createTypedArrayList(Sport.CREATOR);
        page = in.readInt();
        pageSize = in.readInt();
        tempDelta = in.readString();
        delta = in.readLong();
        serverTime = new Date(in.readLong());
        statusCode = in.readInt();
        statusDesc = in.readString();
    }

    public static final Creator<EventInfoByPageListRsp> CREATOR = new Creator<EventInfoByPageListRsp>() {
        @Override
        public EventInfoByPageListRsp createFromParcel(Parcel in) {
            return new EventInfoByPageListRsp(in);
        }

        @Override
        public EventInfoByPageListRsp[] newArray(int size) {
            return new EventInfoByPageListRsp[size];
        }
    };

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeTypedList(sports);
        dest.writeInt(page);
        dest.writeInt(pageSize);
        dest.writeString(tempDelta);
        dest.writeLong(delta);
        dest.writeLong(serverTime != null ? serverTime.getTime() : -1);
        dest.writeInt(statusCode);
        dest.writeString(statusDesc);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getters and setters
    public List<Sport> getSports() {
        return sports;
    }

    public void setSports(List<Sport> sports) {
        this.sports = sports;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getTempDelta() {
        return tempDelta;
    }

    public void setTempDelta(String tempDelta) {
        this.tempDelta = tempDelta;
    }

    public long getDelta() {
        return delta;
    }

    public void setDelta(long delta) {
        this.delta = delta;
    }

    public Date getServerTime() {
        return serverTime;
    }

    public void setServerTime(Date serverTime) {
        this.serverTime = serverTime;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }
}

