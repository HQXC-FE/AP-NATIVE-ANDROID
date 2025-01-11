package com.xtree.live.messenger;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.xtree.base.utils.CfLog;
import com.xtree.service.message.MessageData;
import com.xtree.service.message.MessageType;

import io.sentry.Sentry;

public abstract class LiveInputMessenger implements ILiveInputMessenger {
    private Messenger replyMessenger;

    public LiveInputMessenger() {
    }

    @Override
    public IBinder getBinder() {
        return new Messenger(new Handler(Looper.getMainLooper()) {

            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                MessageType.Input inputType = MessageType.Input.fromCode(msg.what);
                switch (inputType) {
                    case CONNECT:
                        replyMessenger = msg.replyTo;
                        break;
                    default:
                        LiveInputMessenger.this.handleMessage(msg);
                        break;
                }

            }
        }).getBinder();
    }

    private void _sendMessage(MessageType.Output outputType, Parcelable obj) {
        try {

            Message msg = Message.obtain();
            msg.what = outputType.getCode(); // 将 enum 的 code 作为 what 值
            if (obj != null) {
                Bundle data = new Bundle();
                data.putParcelable("data", obj);
                msg.setData(data);
                CfLog.i("Sending class: " + MessageData.class.getName());
            }
            replyMessenger.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
            Sentry.captureException(e);
        }
    }

    @Override
    public void sendMessage(MessageType.Output outputType, Parcelable obj) {
        _sendMessage(outputType, obj);
    }

}
