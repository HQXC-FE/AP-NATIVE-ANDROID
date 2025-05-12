package com.xtree.base.http;

import androidx.annotation.Nullable;

import com.xtree.base.R;
import com.xtree.base.utils.Utils;

import okhttp3.HttpUrl;

public class HijackedException extends RuntimeException {
    private final HttpUrl host;
    private final String responseStr;

    public HijackedException(HttpUrl host, String responseStr) {
        this.host = host;
        this.responseStr = responseStr;
    }


    @Nullable
    @Override
    public String getMessage() {
        String scheme = host.scheme();
        String domain = scheme + "://" + host.host(); // 被劫持域名
        String api = host.url().getPath(); // 被劫持接口
        return Utils.getContext().getString(R.string.text_hijack_tip, domain, api, responseStr);
    }

    /**
     * 获取被拦截域名
     *
     * @return
     */
    public String getHost() {
        return host.host();
    }

    public String getUrl() {
        return host.toString();
    }
}
