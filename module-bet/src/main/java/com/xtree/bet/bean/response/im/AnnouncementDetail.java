package com.xtree.bet.bean.response.im;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

/**
 * 赛事公告详情
 *
 * */


public class AnnouncementDetail implements BaseBean {

    public String languageCode;
    public String content;

    public AnnouncementDetail() {}

    protected AnnouncementDetail(Parcel in) {
        this.languageCode = in.readString();
        this.content = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.languageCode);
        dest.writeString(this.content);
    }

    public void readFromParcel(Parcel source) {
        this.languageCode = source.readString();
        this.content = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AnnouncementDetail> CREATOR = new Creator<AnnouncementDetail>() {
        @Override
        public AnnouncementDetail createFromParcel(Parcel in) {
            return new AnnouncementDetail(in);
        }

        @Override
        public AnnouncementDetail[] newArray(int size) {
            return new AnnouncementDetail[size];
        }
    };
}
