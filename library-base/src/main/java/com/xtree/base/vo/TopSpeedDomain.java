package com.xtree.base.vo;

import android.os.Parcel;

public class TopSpeedDomain implements BaseBean, Comparable<TopSpeedDomain> {
    public String url;
    //测速时间
    public long speedSec;
    //测速评分
    public long speedScore;
    public long curCTSSec = 0;
    //最后一次超时上传的时间
    public long lastUploadMonitor = 0;
    public int isRecommend = 1;//是否推荐 0 或 1

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeLong(this.speedSec);
        dest.writeLong(this.speedScore);
        dest.writeLong(this.curCTSSec);
        dest.writeLong(this.lastUploadMonitor);
    }

    public void readFromParcel(Parcel source) {
        this.url = source.readString();
        this.speedSec = source.readLong();
        this.speedScore = source.readLong();
        this.curCTSSec = source.readLong();
        this.lastUploadMonitor = source.readLong();
    }

    public TopSpeedDomain() {
    }

    protected TopSpeedDomain(Parcel in) {
        this.url = in.readString();
        this.speedSec = in.readLong();
        this.speedScore = in.readLong();
        this.curCTSSec = in.readLong();
        this.lastUploadMonitor = in.readLong();
    }

    public static final Creator<TopSpeedDomain> CREATOR = new Creator<TopSpeedDomain>() {
        @Override
        public TopSpeedDomain createFromParcel(Parcel source) {
            return new TopSpeedDomain(source);
        }

        @Override
        public TopSpeedDomain[] newArray(int size) {
            return new TopSpeedDomain[size];
        }
    };

    @Override
    public int compareTo(TopSpeedDomain o) {
        long result = this.speedSec - o.speedSec;
        if (result > 0) {
            return 1;
        } else if (result == 0) {
            return 0;
        } else {
            return -1;
        }
    }
}
