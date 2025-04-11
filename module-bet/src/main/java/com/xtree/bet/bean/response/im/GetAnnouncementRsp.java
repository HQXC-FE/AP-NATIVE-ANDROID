package com.xtree.bet.bean.response.im;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

import java.util.List;

public class GetAnnouncementRsp implements BaseBean {

    public List<Announcement> announcement;
    public String serverTime;
    public int statusCode;
    public String statusDesc;

    public GetAnnouncementRsp() {}

    protected GetAnnouncementRsp(Parcel in) {
        this.announcement = in.createTypedArrayList(Announcement.CREATOR);
        this.serverTime = in.readString();
        this.statusCode = in.readInt();
        this.statusDesc = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.announcement);
        dest.writeString(this.serverTime);
        dest.writeInt(this.statusCode);
        dest.writeString(this.statusDesc);
    }

    public void readFromParcel(Parcel source) {
        this.announcement = source.createTypedArrayList(Announcement.CREATOR);
        this.serverTime = source.readString();
        this.statusCode = source.readInt();
        this.statusDesc = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GetAnnouncementRsp> CREATOR = new Creator<GetAnnouncementRsp>() {
        @Override
        public GetAnnouncementRsp createFromParcel(Parcel in) {
            return new GetAnnouncementRsp(in);
        }

        @Override
        public GetAnnouncementRsp[] newArray(int size) {
            return new GetAnnouncementRsp[size];
        }
    };
}
