package com.xtree.live.data.source.request;

import com.xtree.base.utils.UuidUtil;

/**
 * Created by KAKA on 2024/10/22.
 * Describe:
 */
public class LiveTokenRequest {
    private String fingerprint = "";
    private String device_type = "android";

    public String getFingerprint() {
        return UuidUtil.getID16();
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
}
