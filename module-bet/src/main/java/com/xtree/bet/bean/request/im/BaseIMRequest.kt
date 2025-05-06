package com.xtree.bet.bean.request.im

data class BaseIMRequest<T>  @JvmOverloads constructor(
    var LanguageCode: String = "CHS",
    var TimeStamp: String = "H3DZU5M2FBrUipvxJGOa6XkicwKu3Qw6Dh33YB5QuEI",
    var format: String = "json",
    var method: String = "post",
    var api: String,
    var data: T
)
