package com.xtree.lottery.data.config

import android.os.Parcelable
import com.xtree.lottery.R
import kotlinx.parcelize.Parcelize

data class LotteryParent(
    val name: String,
    val cate: String,
    val items: List<Any>,
    val list: ArrayList<Lottery>,
)

data class Item(
    val id: Int,
    val name: String? = null
)

@Parcelize
data class Lottery(
    val id: Int,
    val name: String,
    val alias: String,
    val type: String? = null,
    val link: Int,
    val linkType: String,//linkType 分为 ssc   animate  lhc   三种类型
    val curmid: String? = null,
    val handicap: Boolean = true
) : Parcelable

val lotteries = arrayListOf(
    LotteryParent(
        "时时彩", "SSC", listOf(14, 57, 55, 1, 6, 53, 46),
        arrayListOf(
            Lottery(
                id = 14,
                name = "河内五分彩",
                alias = "hn5fc",
                type = "hot",
                link = R.mipmap.lottery_ssc_1,
                linkType = "ssc",
                curmid = "2309",
            ),
            Lottery(
                id = 57,
                name = "经典重庆时时彩",
                alias = "jdcqssc",
                type = "new",
                link = R.mipmap.lottery_ssc_2,
                linkType = "ssc",
                curmid = "400204",
                handicap = false
            ),
            Lottery(
                id = 55,
                name = "重庆5分彩",
                alias = "cq5fc",
                type = "new",
                link = R.mipmap.lottery_ssc_3,
                linkType = "ssc",
                curmid = "399867",
                handicap = false
            ),
            Lottery(
                id = 1,
                name = "重庆时时彩",
                alias = "cqssc",
                link = R.mipmap.lottery_ssc_4,
                linkType = "ssc",
                curmid = "50",
                handicap = false
            ),
            Lottery(
                id = 6,
                name = "新疆时时彩",
                alias = "xjssc",
                link = R.mipmap.lottery_ssc_5,
                linkType = "ssc",
                curmid = "220",
                handicap = false
            ),
            Lottery(
                id = 53,
                name = "腾讯5分彩",
                alias = "txwfc",
                type = "new",
                link = R.mipmap.lottery_ssc_6,
                linkType = "ssc",
                curmid = "399350"
            ),
            Lottery(
                id = 46,
                name = "泰国300秒",
                alias = "tg300s",
                link = R.mipmap.lottery_ssc_7,
                linkType = "ssc",
                curmid = "376050",
                handicap = false
            ),
        ),
    ),

    LotteryParent(
        "分分彩",
        "FFC",
        listOf(19, 28, 42, 52, 38, 39, 40, 29, 54, 51, 41, 58, 26, 27, 47, Item(23, "意昂秒秒彩")),
        arrayListOf(
            Lottery(
                id = 19,
                name = "河内一分彩",
                alias = "hn1fc",
                type = "hot",
                link = R.mipmap.lottery_ffc_1,
                linkType = "ssc",
                curmid = "19000"
            ),
            Lottery(
                id = 28,
                name = "腾讯分分彩",
                alias = "txffc",
                type = "hot",
                link = R.mipmap.lottery_ffc_2,
                linkType = "ssc",
                curmid = "338050"
            ),
            Lottery(
                id = 42,
                name = "多彩腾讯30秒",
                alias = "tx30s",
                type = "new",
                link = R.mipmap.lottery_ffc_3,
                linkType = "ssc",
                curmid = "364050",
                handicap = false
            ),
            Lottery(
                id = 52,
                name = "腾讯2分彩(双)",
                alias = "txssq",
                link = R.mipmap.lottery_ffc_4,
                linkType = "ssc",
                curmid = "399200",
                handicap = false
            ),
            Lottery(
                id = 38,
                name = "日本分分彩",
                alias = "rbffc",
                link = R.mipmap.lottery_ffc_5,
                linkType = "ssc",
                curmid = "352050",
                handicap = false
            ),
            Lottery(
                id = 39,
                name = "西贡分分彩",
                alias = "xgffc",
                link = R.mipmap.lottery_ffc_6,
                linkType = "ssc",
                curmid = "355050",
                handicap = false
            ),
            Lottery(
                id = 40,
                name = "印尼分分彩",
                alias = "ynffc",
                type = "new",
                link = R.mipmap.lottery_ffc_7,
                linkType = "ssc",
                curmid = "358050",
                handicap = false
            ),
            Lottery(
                id = 29,
                name = "加拿大30秒",
                alias = "jnd30m",
                type = "hot",
                link = R.mipmap.lottery_ffc_8,
                linkType = "ssc",
                curmid = "341050",
                handicap = false
            ),
            Lottery(
                id = 54,
                name = "重庆分分彩",
                alias = "cqffc",
                type = "new",
                link = R.mipmap.lottery_ffc_9,
                linkType = "ssc",
                curmid = "399507",
                handicap = false
            ),
            Lottery(
                id = 51,
                name = "腾讯2分彩(单)",
                alias = "txdsq",
                link = R.mipmap.lottery_ffc_10,
                linkType = "ssc",
                curmid = "399050",
                handicap = false
            ),
            Lottery(
                id = 41,
                name = "多彩重庆30秒",
                alias = "cq30s",
                type = "new",
                link = R.mipmap.lottery_ffc_11,
                linkType = "ssc",
                curmid = "361050"
            ),
            Lottery(
                id = 58,
                name = "奇趣分分",
                alias = "sytxffc",
                link = R.mipmap.lottery_ffc_12,
                linkType = "ssc",
                curmid = "400500",
                handicap = false
            ),
            Lottery(
                id = 26,
                name = "瑞典1分彩",
                alias = "rd1fc",
                link = R.mipmap.lottery_ffc_13,
                linkType = "ssc",
                curmid = "314050",
                handicap = false
            ),
            Lottery(
                id = 27,
                name = "瑞典2分彩",
                alias = "rd2fc",
                link = R.mipmap.lottery_ffc_14,
                linkType = "ssc",
                curmid = "317050"
            ),
            Lottery(
                id = 47,
                name = "泰国60秒",
                alias = "tg60s",
                link = R.mipmap.lottery_ffc_15,
                linkType = "ssc",
                curmid = "379050",
                handicap = false
            ),
            Lottery(
                id = 23,
                name = "意昂" + "秒秒彩",
                alias = "mmc",
                link = R.mipmap.lottery_ffc_16,
                linkType = "ssc",
                curmid = "311700",
                handicap = false
            ),
        )
    ),
    LotteryParent(
        "虚拟币彩", "XNBC", listOf(83, 84, 85, 86, 91, 92, 93),
        arrayListOf(
            Lottery(
                id = 83,
                name = "币安比特币分分彩",
                alias = "bnbtcffc",
                type = "new",
                link = R.mipmap.lottery_xnbc_1,
                linkType = "ssc",
                curmid = "404075"
            ),
            Lottery(
                id = 84,
                name = "币安比特币五分彩",
                alias = "bnbtcwfc",
                type = "new",
                link = R.mipmap.lottery_xnbc_2,
                linkType = "ssc",
                curmid = "404278"
            ),
            Lottery(
                id = 85,
                name = "币安以太币分分彩",
                alias = "bnethffc",
                type = "new",
                link = R.mipmap.lottery_xnbc_3,
                linkType = "ssc",
                curmid = "404481"
            ),
            Lottery(
                id = 86,
                name = "币安以太币五分彩",
                alias = "bnethwfc",
                type = "new",
                link = R.mipmap.lottery_xnbc_4,
                linkType = "ssc",
                curmid = "404684"
            ),
            Lottery(
                id = 91,
                name = "哈希分分彩",
                alias = "hashffc",
                type = "new",
                link = R.mipmap.lottery_xnbc_5,
                linkType = "ssc",
                curmid = "405699"
            ),
            Lottery(
                id = 92,
                name = "哈希三分彩",
                alias = "hashsfc",
                type = "new",
                link = R.mipmap.lottery_xnbc_6,
                linkType = "ssc",
                curmid = "405902",
                handicap = false
            ),
            Lottery(
                id = 93,
                name = "哈希五分彩",
                alias = "hashwfc",
                type = "new",
                link = R.mipmap.lottery_xnbc_7,
                linkType = "ssc",
                curmid = "406105",
                handicap = false
            ),
        )
    ),
    LotteryParent(
        "11选5", "11X5", listOf(35, 8, 7, 5, 43, 48, 75, 76, 77, 78, 79),
        arrayListOf(
            Lottery(
                id = 35,
                name = "加拿大11选5",
                alias = "jnd11x5",
                type = "hot",
                link = R.mipmap.lottery_11x5_1,
                linkType = "ssc",
                curmid = "351402",
                handicap = false
            ),
            Lottery(
                id = 8,
                name = "广东11选5",
                alias = "gd11x5",
                link = R.mipmap.lottery_11x5_2,
                linkType = "ssc",
                curmid = "302",
                handicap = false
            ),
            Lottery(
                id = 7,
                name = "江西11选5",
                alias = "jx11x5",
                link = R.mipmap.lottery_11x5_3,
                linkType = "ssc",
                curmid = "256",
                handicap = false
            ),
            Lottery(
                id = 5,
                name = "山东11选5",
                alias = "sd11x5",
                link = R.mipmap.lottery_11x5_4,
                linkType = "ssc",
                curmid = "174",
                handicap = false
            ),
            Lottery(
                id = 43,
                name = "江苏11选5",
                alias = "js11x5",
                link = R.mipmap.lottery_11x5_5,
                linkType = "ssc",
                curmid = "366420",
                handicap = false
            ),
            Lottery(
                id = 48,
                name = "泰国11选5",
                alias = "tg11x5",
                link = R.mipmap.lottery_11x5_6,
                linkType = "ssc",
                curmid = "390402",
                handicap = false
            ),
            Lottery(
                id = 75,
                name = "黑龙江11选5",
                alias = "hlj11x5",
                link = R.mipmap.lottery_11x5_7,
                linkType = "ssc",
                curmid = "403813",
                handicap = false
            ),
            Lottery(
                id = 76,
                name = "湖北11选5",
                alias = "hb11x5",
                link = R.mipmap.lottery_11x5_8,
                linkType = "ssc",
                curmid = "403849",
                handicap = false
            ),
            Lottery(
                id = 77,
                name = "辽宁11选5",
                alias = "ln11x5",
                link = R.mipmap.lottery_11x5_9,
                linkType = "ssc",
                curmid = "403885",
                handicap = false
            ),
            Lottery(
                id = 78,
                name = "广西11选5",
                alias = "gx11x5",
                link = R.mipmap.lottery_11x5_10,
                linkType = "ssc",
                curmid = "403921",
                handicap = false
            ),
            Lottery(
                id = 79,
                name = "云南11选5",
                alias = "yn11x5",
                link = R.mipmap.lottery_11x5_11,
                linkType = "ssc",
                curmid = "403957",
                handicap = false
            ),
        )
    ),
    LotteryParent(
        "PK10/赛马", "PK10", listOf(50, 24, 25, 34),
        arrayListOf(
            Lottery(
                id = 50,
                name = "幸运飞艇",
                alias = "xyft",
                type = "new",
                link = R.mipmap.lottery_pk10_1,
                linkType = "ssc",
                curmid = "399000",
                handicap = false
            ),
            Lottery(
                id = 24,
                name = "北京PK拾",
                alias = "pk10",
                type = "hot",
                link = R.mipmap.lottery_pk10_2,
                linkType = "ssc",
                curmid = "313012",
                handicap = false
            ),
            Lottery(
                id = 25,
                name = "急速赛马",
                alias = "jssm",
                type = "hot",
                link = R.mipmap.lottery_pk10_3,
                linkType = "ssc",
                curmid = "313212",
                handicap = false
            ),
            Lottery(
                id = 34,
                name = "意大利PK拾",
                alias = "ydl10",
                type = "hot",
                link = R.mipmap.lottery_pk10_4,
                linkType = "ssc",
                curmid = "347712",
                handicap = false
            ),
        )
    ),
    LotteryParent(
        "其他", "OTHER", listOf(37, 11, 12, 20, 73, 80, 81, 82),
        arrayListOf(
            Lottery(
                id = 37,
                name = "加拿大3D",
                alias = "3djnd",
                type = "hot",
                link = R.mipmap.lottery_other_1,
                linkType = "ssc",
                curmid = "351451",
                handicap = false
            ),

            Lottery(
                id = 11,
                name = "3D福彩",
                alias = "3dfc",
                link = R.mipmap.lottery_other_2,
                linkType = "ssc",
                curmid = "614",
                handicap = false
            ),
            Lottery(
                id = 12,
                name = "排列3",
                alias = "pl3",
                link = R.mipmap.lottery_other_3,
                linkType = "ssc",
                curmid = "615",
                handicap = false
            ),
            Lottery(
                id = 20,
                name = "香港六合彩",
                alias = "xglhc",
                link = R.mipmap.lottery_other_4,
                linkType = "lhc",
                curmid = "20000",
                handicap = false
            ),
            Lottery(
                id = 73,
                name = "澳门六合彩",
                alias = "amlhc",
                link = R.mipmap.lottery_other_5,
                linkType = "lhc",
                curmid = "403649",
                handicap = false
            ),
            Lottery(
                id = 80,
                name = "湖北快三",
                alias = "hbk3",
                link = R.mipmap.lottery_other_6,
                linkType = "ssc",
                curmid = "403993",
                handicap = false
            ),
            Lottery(
                id = 81,
                name = "河南快三",
                alias = "hnks",
                link = R.mipmap.lottery_other_7,
                linkType = "ssc",
                curmid = "404016",
                handicap = false
            ),
            Lottery(
                id = 82,
                name = "吉林快三",
                alias = "jlks",
                link = R.mipmap.lottery_other_8,
                linkType = "ssc",
                curmid = "404039",
                handicap = false
            ),
        )
    )
)
