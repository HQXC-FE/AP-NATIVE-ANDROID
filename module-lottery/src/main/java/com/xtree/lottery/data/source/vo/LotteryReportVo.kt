package com.xtree.lottery.data.source.vo

data class LotteryReportVo(
    val aProject: List<LotteryOrderVo>,
    val customer_service_link: String,
    val desK: String,
    val endtime: String,
    val isgetDate: Int,
    val lotteryList: List<Lottery>,
    val lotteryid: String,
    val methodid: String,
    val mobile_page: MobilePage,
    val newMethodList: String,
    val pageinfo: String,
    val pt_download_pc: String,
    val pub_channel_id: String,
    val pub_channel_token: String,
    val push_server_host: String,
    val push_service_module: String,
    val push_service_status: Int,
    val quickSearch: String,
    val sSystemImagesAndCssPath: String,
    val starttime: String,
    val today: String,
    val topprizes_herolist_enabled: String,
    val topprizes_publicity_enabled: String,
    val topprizes_wintips_enabled: String,
    //val total: Total,
    val user: User,
    val user_channel_id: String,
    val username: String,
    val webtitle: String
)

data class AProject(
    val bonus: String,
    val canneldeadline: String,
    val code: String,
    val code_num: String,
    val codes_str: String,
    val i_code: String,
    val ii: Int,
    val iscancel: String,
    val isgetprize: String,
    val issue: String,
    val lotteryid: String,
    val lotteryname: String,
    val methodid: String,
    val methodname: String,
    val modes: String,
    val multiple: String,
    val packageid: String,
    val prizestatus: String,
    val projectid: String,
    val projectidCopy: String,
    val statuscode: String,
    val totalprice: String,
    val userid: String,
    val username: String,
    val writetime: String
)

data class Lottery(
    val cnname: String,
    val lotteryid: String,
    val lotterytype: String
)

data class MobilePage(
    val p: String,
    val page_size: String,
    val total_page: String
)

data class Total(
    val bet: String,
    val win: String
)

data class User(
    val availablebalance: String,
    val iscreditaccount: String,
    val messages: String,
    val nickname: String,
    val parentid: String,
    val preinfo: String,
    val userrank: String,
    val usertype: String
)