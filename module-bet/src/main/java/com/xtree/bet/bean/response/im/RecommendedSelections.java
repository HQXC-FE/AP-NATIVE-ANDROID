package com.xtree.bet.bean.response.im;


import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

import java.util.List;

public class RecommendedSelections implements BaseBean {


    public List<RecommendedSport> Sports;
    public Long TempDelta;
    public Long Delta;
    public String ServerTime;
    public int StatusCode;
    public String StatusDesc;


    // 构造方法（从 Parcel 读取）
    protected RecommendedSelections(Parcel in) {
        // 读取 List<Sport>
        Sports  = in.createTypedArrayList(RecommendedSport.CREATOR);

        // 读取可能为 null 的 Object
        if (in.readByte() == 0) {
            TempDelta = null;
        } else {
            TempDelta = in.readLong();
        }

        // 读取基本类型
        Delta = in.readLong();
        ServerTime = in.readString();
        StatusCode = in.readInt();
        StatusDesc = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(Sports);          // 写入集合
        dest.writeValue(TempDelta);      // 支持 Object
        dest.writeLong(Delta);
        dest.writeString(ServerTime);
        dest.writeInt(StatusCode);
        dest.writeString(StatusDesc);
    }

    // 自动生成 Creator
    public static final Creator<RecommendedSelections> CREATOR = new Creator<RecommendedSelections>() {
        @Override
        public RecommendedSelections createFromParcel(Parcel in) {
            return new RecommendedSelections(in);
        }

        @Override
        public RecommendedSelections[] newArray(int size) {
            return new RecommendedSelections[size];
        }
    };

    @Override
    public int describeContents() {
        return 0; // 默认返回 0
    }


}
