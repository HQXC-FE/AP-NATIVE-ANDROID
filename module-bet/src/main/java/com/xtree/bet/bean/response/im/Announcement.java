package com.xtree.bet.bean.response.im;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

import java.util.List;

public class Announcement implements BaseBean {

    public int announcementId;
    public String postingDate;
    public String expiryDate;
    public String dateUpdated;
    public List<AnnouncementDetail> announcementDetail;

    public Announcement() {}

    protected Announcement(Parcel in) {
        this.announcementId = in.readInt();
        this.postingDate = in.readString();
        this.expiryDate = in.readString();
        this.dateUpdated = in.readString();
        this.announcementDetail = in.createTypedArrayList(AnnouncementDetail.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.announcementId);
        dest.writeString(this.postingDate);
        dest.writeString(this.expiryDate);
        dest.writeString(this.dateUpdated);
        dest.writeTypedList(this.announcementDetail);
    }

    public void readFromParcel(Parcel source) {
        this.announcementId = source.readInt();
        this.postingDate = source.readString();
        this.expiryDate = source.readString();
        this.dateUpdated = source.readString();
        this.announcementDetail = source.createTypedArrayList(AnnouncementDetail.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Announcement> CREATOR = new Creator<Announcement>() {
        @Override
        public Announcement createFromParcel(Parcel in) {
            return new Announcement(in);
        }

        @Override
        public Announcement[] newArray(int size) {
            return new Announcement[size];
        }
    };
}
