package com.xtree.service.message;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class RemoteMessage implements Parcelable {

    public static final Creator<RemoteMessage> CREATOR = new Creator<RemoteMessage>() {
        @Override
        public RemoteMessage createFromParcel(Parcel in) {
            return new RemoteMessage(in);
        }

        @Override
        public RemoteMessage[] newArray(int size) {
            return new RemoteMessage[size];
        }
    };

    private String type;
    private int fd;
    private long timestamp;
    private List<MessageData> data; // 使用 List<MessageData> 来适应单个对象或数组

    // Constructors
    public RemoteMessage(String type, int fd, long timestamp, List<MessageData> data) {
        this.type = type;
        this.fd = fd;
        this.timestamp = timestamp;
        this.data = data;
    }

    protected RemoteMessage(Parcel in) {
        type = in.readString();
        fd = in.readInt();
        timestamp = in.readLong();
        data = in.createTypedArrayList(MessageData.CREATOR); // 使用 createTypedArrayList
    }

    // Getters and Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getFd() {
        return fd;
    }

    public void setFd(int fd) {
        this.fd = fd;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List<MessageData> getData() {
        return data;
    }

    public void setData(List<MessageData> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RemoteMessage{" +
                "type='" + type + '\'' +
                ", fd=" + fd +
                ", timestamp=" + timestamp +
                ", data=" + data +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int flags) {
        parcel.writeString(type);
        parcel.writeInt(fd);
        parcel.writeLong(timestamp);
        parcel.writeTypedList(data); // 使用 writeTypedList
    }


}
