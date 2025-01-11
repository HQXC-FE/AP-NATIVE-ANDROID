package com.xtree.live.messenger;

import android.os.Message;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.xtree.service.message.MessageType;
import com.xtree.service.messenger.IMessenger;

import javax.annotation.Nullable;

public interface ILiveInputMessenger extends IMessenger {

    public void sendMessage(@NonNull MessageType.Output outputType, @Nullable Parcelable obj);

    public void handleMessage(Message msg);

}
