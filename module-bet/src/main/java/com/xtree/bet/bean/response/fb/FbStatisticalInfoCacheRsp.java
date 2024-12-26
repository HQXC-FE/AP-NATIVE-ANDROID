package com.xtree.bet.bean.response.fb;

import android.annotation.SuppressLint;
import android.os.Parcel;

import androidx.annotation.NonNull;

import com.xtree.base.vo.BaseBean;

/**
 * 赛事统计缓存接口请求数据
 */
public class FbStatisticalInfoCacheRsp {
    private int code;
    private String msg;
    public StatisticalInfo data = new StatisticalInfo();

    public StatisticalInfo getData() {
        return data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

}
