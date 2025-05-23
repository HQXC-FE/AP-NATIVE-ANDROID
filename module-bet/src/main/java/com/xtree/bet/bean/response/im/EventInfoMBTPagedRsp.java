package com.xtree.bet.bean.response.im;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.xtree.base.vo.BaseBean;

import java.util.Date;
import java.util.List;

public class EventInfoMBTPagedRsp implements BaseBean, Parcelable {
    private int page;
    private int pageSize;
    private int totalPage;
    private String total;
    private List<MatchInfo> events;

    public EventInfoMBTPagedRsp() {
    }

    protected EventInfoMBTPagedRsp(Parcel in) {
        page = in.readInt();
        pageSize = in.readInt();
        totalPage = in.readInt();
        total = in.readString();
        events = in.createTypedArrayList(MatchInfo.CREATOR);
    }

    public static final Creator<EventInfoMBTPagedRsp> CREATOR = new Creator<EventInfoMBTPagedRsp>() {
        @Override
        public EventInfoMBTPagedRsp createFromParcel(Parcel in) {
            return new EventInfoMBTPagedRsp(in);
        }

        @Override
        public EventInfoMBTPagedRsp[] newArray(int size) {
            return new EventInfoMBTPagedRsp[size];
        }
    };

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(page);
        dest.writeInt(pageSize);
        dest.writeInt(totalPage);
        dest.writeString(total);
        dest.writeTypedList(events);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getters and Setters

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

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<MatchInfo> getEvents() {
        return events;
    }

    public void setEvents(List<MatchInfo> events) {
        this.events = events;
    }
}

