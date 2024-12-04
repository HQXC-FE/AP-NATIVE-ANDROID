package com.xtree.lottery.data.source.vo

data class TraceInfoVo(
    val customer_service_link: String,
    val desK: String,
    val endtime: String,
    val isgetDate: Int,
    val list: List<Bean>,
    //val lotteryList: LotteryList,
    val lotteryid: Int,
    val methodList: String,
    val methodid: Int,
    val mobile_page: MobilePage,
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
    val total_sum: TotalSum,
    val user: User,
    val user_channel_id: String,
    val username: String,
    val webtitle: String
) {

    data class MobilePage(
        val p: Int,
        val page_size: Int,
        val total_page: String
    )

    data class TotalSum(
        val total_cancelprice: String,
        val total_finishprice: String,
        val total_taskprice: String
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

    data class Bean(
        val beginissue: String,
        val begintime: String,
        val cancelprice: String,
        val cnname: String,
        val codes: String,
        val codes_str: String,
        val finishprice: String,
        val ii: Int,
        val lotteryid: String,
        val methodid: String,
        val methodname: String,
        val status: String,
        val stoponwin: String,
        val taskid: String,
        val taskprice: String
    )

    data class X0(
        val cnname: String,
        val lotteryid: String,
        val lotterytype: Int
    )
}