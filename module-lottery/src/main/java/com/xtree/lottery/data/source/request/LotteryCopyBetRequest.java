package com.xtree.lottery.data.source.request;

import com.google.gson.annotations.SerializedName;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.base.utils.UuidUtil;

import java.util.List;

/**
 * Created by KAKA on 2024/5/10.
 * Describe:
 */
public class LotteryCopyBetRequest {

    //{
    //  "id": "D20250116-149VCFEBEBAHBJ",
    //  "play_source": 6,
    //  "nonce": "09b4633cb42e4f18cf9c5909324d8f44"
    //}

    /**
     * curmid
     */
    @SerializedName("id")
    private String id;

    /**
     * playSource
     */
    @SerializedName("play_source")
    private int play_source;
    /**
     * nonce
     */
    @SerializedName("nonce")
    private String nonce;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPlay_source() {
        return play_source;
    }

    public void setPlay_source(int play_source) {
        this.play_source = play_source;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }
}
