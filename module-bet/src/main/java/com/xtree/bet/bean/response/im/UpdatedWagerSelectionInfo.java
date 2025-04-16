package com.xtree.bet.bean.response.im;

import android.os.Parcel;
import android.os.Parcelable;

import com.xtree.base.vo.BaseBean;

public  class UpdatedWagerSelectionInfo implements BaseBean {
        // 根据实际字段补充
        @Override
        public void writeToParcel(Parcel dest, int flags) {}

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<UpdatedWagerSelectionInfo> CREATOR = new Creator<UpdatedWagerSelectionInfo>() {
            @Override
            public UpdatedWagerSelectionInfo createFromParcel(Parcel in) {
                return new UpdatedWagerSelectionInfo();
            }

            @Override
            public UpdatedWagerSelectionInfo[] newArray(int size) {
                return new UpdatedWagerSelectionInfo[size];
            }
        };
    }