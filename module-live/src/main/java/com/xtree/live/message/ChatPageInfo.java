package com.xtree.live.message;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Objects;

public class ChatPageInfo implements Parcelable {
    public final String defaultFirstPage;
    public String lastMsgId;
    public ChatPageInfo(String defaultFirstPage) {
        this.defaultFirstPage = defaultFirstPage;
        this.lastMsgId = this.defaultFirstPage;
    }

    public ChatPageInfo() {
        this.defaultFirstPage = "0";
        this.lastMsgId = this.defaultFirstPage;
    }


    protected ChatPageInfo(Parcel in) {
        defaultFirstPage = in.readString();
        lastMsgId = in.readString();
    }

    public static final Creator<ChatPageInfo> CREATOR = new Creator<ChatPageInfo>() {
        @Override
        public ChatPageInfo createFromParcel(Parcel in) {
            return new ChatPageInfo(in);
        }

        @Override
        public ChatPageInfo[] newArray(int size) {
            return new ChatPageInfo[size];
        }
    };



    public void reset() {
        lastMsgId = defaultFirstPage;
    }

    public void nextPage(String lastMsgId){
        this.lastMsgId = lastMsgId;
    }
    public boolean isFirstPage() {
        return Objects.equals(lastMsgId, defaultFirstPage);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(defaultFirstPage);
        dest.writeString(lastMsgId);
    }
}

