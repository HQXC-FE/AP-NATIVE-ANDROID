package com.xtree.live.ui.main.model.chat;

import com.xtree.base.net.live.X9LiveInfo;
import com.xtree.live.LiveConfig;

public class LiveThiredLoginRequest {
    public LiveThiredLoginRequest(){
        super();
        this.fingerprint = X9LiveInfo.INSTANCE.getOaid();
        this.device_type = "android";
        this.user_id = LiveConfig.getUserId() ;

    }
    public LiveThiredLoginRequest(String fingerprint, String device_type, String user_id){
        this.fingerprint = fingerprint;
        this.device_type = device_type;
        this.user_id = user_id ;
    }
    private String fingerprint = X9LiveInfo.INSTANCE.getOaid() ;
    private String device_type ;
    private String user_id;

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
