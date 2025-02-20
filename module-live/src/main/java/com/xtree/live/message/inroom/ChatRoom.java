package com.xtree.live.message.inroom;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.xtree.live.message.RoomType;

public class ChatRoom implements Parcelable {
    public String vid;
    public @RoomType int roomType;
    public  String name;
    public ChatRoom(int roomType, String vid, String name) {
        this.vid = vid;
        this.roomType = roomType;
        this.name = name;
    }

    protected ChatRoom(Parcel in) {
        vid = in.readString();
        roomType = in.readInt();
        name = in.readString();
    }

    public static final Creator<ChatRoom> CREATOR = new Creator<ChatRoom>() {
        @Override
        public ChatRoom createFromParcel(Parcel in) {
            return new ChatRoom(in);
        }

        @Override
        public ChatRoom[] newArray(int size) {
            return new ChatRoom[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(vid);
        dest.writeInt(roomType);
        dest.writeString(name);
    }
}
