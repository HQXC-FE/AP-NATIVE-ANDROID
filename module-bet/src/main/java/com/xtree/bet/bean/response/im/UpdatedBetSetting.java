package com.xtree.bet.bean.response.im;

import android.os.Parcel;
import android.os.Parcelable;

import com.xtree.base.vo.BaseBean;

public  class UpdatedBetSetting implements BaseBean {
        // 根据实际字段补充
        @Override
        public void writeToParcel(Parcel dest, int flags) {}

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<UpdatedBetSetting> CREATOR = new Creator<UpdatedBetSetting>() {
            @Override
            public UpdatedBetSetting createFromParcel(Parcel in) {
                return new UpdatedBetSetting();
            }

            @Override
            public UpdatedBetSetting[] newArray(int size) {
                return new UpdatedBetSetting[size];
            }
        };
    }