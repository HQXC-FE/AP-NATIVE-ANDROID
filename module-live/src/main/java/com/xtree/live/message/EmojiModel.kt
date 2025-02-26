package com.xtree.live.message

import com.google.gson.annotations.SerializedName

data class EmojiModel(
    @SerializedName("emojiurl")
    val emojiUrl: String?,
    @SerializedName("group_name")
    val groupName: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("thumburl")
    val thumbUrl: String?
)
