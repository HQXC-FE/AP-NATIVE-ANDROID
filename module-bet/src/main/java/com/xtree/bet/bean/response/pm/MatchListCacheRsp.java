package com.xtree.bet.bean.response.pm;

import android.os.Build;
import android.os.Parcel;

import androidx.annotation.NonNull;

import com.xtree.base.vo.BaseBean;

import java.util.ArrayList;
import java.util.List;

public class MatchListCacheRsp implements BaseBean {
    private String cto;
    private int pages;
    private Data data;
    //public List<MatchInfo> data = new ArrayList<>();

    public int getPages() {
        return pages;
    }

    public Data getData() {
        return data;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cto);
        dest.writeInt(this.pages);
        dest.writeTypedObject(this.data, 0);

    }

    public void readFromParcel(Parcel source) {
        this.cto = source.readString();
        this.pages = source.readInt();
        this.data = source.readTypedObject(Data.CREATOR);

    }

    public MatchListCacheRsp() {
    }

    protected MatchListCacheRsp(Parcel in) {
        this.cto = in.readString();
        this.pages = in.readInt();
        this.data = in.readTypedObject(Data.CREATOR);
    }

    public static final Creator<MatchListCacheRsp> CREATOR = new Creator<MatchListCacheRsp>() {
        @Override
        public MatchListCacheRsp createFromParcel(Parcel source) {
            return new MatchListCacheRsp(source);
        }

        @Override
        public MatchListCacheRsp[] newArray(int size) {
            return new MatchListCacheRsp[size];
        }
    };

    public static class Data implements BaseBean {
        private int code;
        private List<MatchInfo> data = new ArrayList<>(); // List of integers
        private String msg;

        public Data(Parcel source) {
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public List<MatchInfo> getData() {
            return data;
        }

        public static final Creator<MatchListCacheRsp.Data> CREATOR = new Creator<MatchListCacheRsp.Data>() {
            @Override
            public MatchListCacheRsp.Data createFromParcel(Parcel source) {
                return new MatchListCacheRsp.Data(source);
            }

            @Override
            public MatchListCacheRsp.Data[] newArray(int size) {
                return new Data[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel dest, int flags) {

        }
    }
}
