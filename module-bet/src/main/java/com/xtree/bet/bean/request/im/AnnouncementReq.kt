package com.xtree.bet.bean.request.im

import com.google.gson.annotations.SerializedName

class AnnouncementReq {
    var api = "GetAnnouncement"
    var method = "post"
    var format = "json"

    @SerializedName("TimeStamp")
    var timeStamp: String? = null
    var languageCode = "chz"

    override fun toString(): String {
        return "AnnouncementReq{" +
                "api='" + api + '\'' +
                ", method='" + method + '\'' +
                ", format='" + format + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", languageCode='" + languageCode + '\'' +
                '}'
    }
}
