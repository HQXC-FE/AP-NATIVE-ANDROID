package com.xtree.live.message;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.hjq.gson.factory.GsonFactory;
import com.xtree.live.message.inroom.InRoomData;
import com.xtree.live.message.inroom.InRoomExtra;

public class MessagePin extends MessageChat{

    @SerializedName("token")
    private String token;
    @SerializedName("pin")
    private int pin;
    @SerializedName("msg_id")
    private long msgId;
    @SerializedName("pinType")
    private int pinType;
    @SerializedName("data")
    private String data;

    @SerializedName("extra")
    private String extra;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public int getPinType() {
        return pinType;
    }

    public void setPinType(int pinType) {
        this.pinType = pinType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public InRoomData parseInRoomData(){
        InRoomData inRoomBean = new InRoomData();
        inRoomBean.setPinType(pinType);
        Gson gson = GsonFactory.getSingletonGson();
        //聊天广场广告
        if(1 == pinType){
            InRoomExtra extraBean =  gson.fromJson(extra, InRoomExtra.class);
            inRoomBean.setPinData("");
            JsonObject pinData =  gson.fromJson(getData(), JsonObject.class);
            if(pinData != null && pinData.get("text") != null){
                inRoomBean.setPinData(pinData.get("text").getAsString());
            }
            inRoomBean.setExtra(extraBean);
        }
        //群组消息置顶
        if(0 == pinType){
            JsonElement pinObj = GsonFactory.getSingletonGson().fromJson(data, new TypeToken<>() {});
            inRoomBean.setPinObj(pinObj);
            inRoomBean.setPinType(pinType);
        }
        return inRoomBean;
    }
}

