package com.xtree.live.socket;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class BNCObject implements BNC, Parcelable {
    private final String bncImagePath;
    private final String objectKey;

    public BNCObject(@NonNull String bncImagePath, @NonNull String objectKey) {
        this.bncImagePath = bncImagePath;
        this.objectKey = objectKey;
    }

    public BNCObject(BNC bnc) {
        this.bncImagePath = bnc.imagePath();
        this.objectKey = bnc.objectKey();
    }

    protected BNCObject(Parcel in) {
        bncImagePath = in.readString();
        objectKey = in.readString();
    }

    public static final Creator<BNCObject> CREATOR = new Creator<BNCObject>() {
        @Override
        public BNCObject createFromParcel(Parcel in) {
            return new BNCObject(in);
        }

        @Override
        public BNCObject[] newArray(int size) {
            return new BNCObject[size];
        }
    };

    @Override
    public @NonNull String imagePath() {
        return bncImagePath;
    }

    @Override
    public @NonNull String objectKey() {
        return objectKey;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(bncImagePath);
        dest.writeString(objectKey);
    }
}
