package com.xtree.lottery.rule.betting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Matchers {
    public static final Map<String, List<String>> TOKENS = Map.ofEntries(
            Map.entry("MULTI", List.of(
                    "五星-五星直选-复式",
                    "四星-四星直选-复式",
                    "后三码-后三直选-复式",
                    "三码-后三直选-复式",
                    "前三码-前三直选-复式",
                    "中三码-中三直选-复式",
                    "二码-二星直选-后二直选(复式)",
                    "二码-二星直选-前二直选(复式)",
                    "二码-二星直选-中二直选(复选)",
                    "大小单双-大小单双-后二",
                    "大小单双-大小单双-前二",
                    "后三码-后三直选-直选复式",
                    "前三码-前三直选-直选复式",
                    "二码-二星直选-前二直选复式",
                    "二码-二星直选-后二直选复式",
                    "二同号单选-二同号单选-标准选号",
                    "三码-直选-复式",
                    "二码-二码-后二直选复式",
                    "二码-二星-后二直选复式",
                    "二码-二星-前二直选复式",
                    "二星-二星-后二直选复式",
                    "二星-二星-前二直选复式",
                    "大小单双-大小单双组合-前三",
                    "大小单双-大小单双组合-后三",
                    "大小单双-大小单双组合-后二",
                    "大小单双-大小单双组合-前二",
                    "后三码-后三码-复式",
                    "前三码-前三码-复式",
                    "二码-后二码-复式",
                    "尾-后二-复式",
                    "一等奖-后二-复式",
                    "尾-后三-复式",
                    "一等奖-后三-复式",
                    "尾-后四-复式",
                    "一等奖-后四-复式"
            )),
            Map.entry("SINGLE_5", List.of(
                    "五星-五星直选-单式"
            )),
            Map.entry("COMBINATION", List.of(
                    "五星-五星直选-组合",
                    "四星-四星直选-组合"
            )),
            Map.entry("MULTI_COMBINATION", List.of(
                    "五星-五星组选-组选120",
                    "五星-五星组选-组选60 ",
                    "五星-五星组选-组选30",
                    "五星-五星组选-组选20",
                    "五星-五星组选-组选10",
                    "五星-五星组选-组选5",
                    "四星-四星组选-组选24",
                    "四星-四星组选-组选12",
                    "四星-四星组选-组选6",
                    "四星-四星组选-组选4",
                    "五星-五星组选-组选60"
            )),
            Map.entry("SINGLE_4", List.of(
                    "四星-四星直选-单式",
                    "尾-后四-单式",
                    "一等奖-后四-单式"
            )),
            Map.entry("SINGLE_3", List.of(
                    "后三码-后三码-单式",
                    "前三码-前三码-单式",
                    "后三码-后三直选-单式",
                    "三码-后三直选-单式",
                    "前三码-前三直选-单式",
                    "中三码-中三直选-单式",
                    "后三码-后三直选-直选单式",
                    "前三码-前三直选-直选单式",
                    "三码-直选-单式",
                    "尾-后三-单式",
                    "一等奖-后三-单式"
            )),
            Map.entry("SUM_PLUS_DIRECT_3", List.of(
                    "后三码-后三直选-直选和值",
                    "前三码-前三直选-直选和值",
                    "中三码-中三直选-直选和值",
                    "三码-直选-直选和值",
                    "三码-直选-和值"
            )),
            Map.entry("MULTI_COMBINATION_3", List.of(
                    "后三码-后三组选-组三",
                    "前三码-前三组选-组三",
                    "中三码-中三组选-组三",
                    "三码-组选-组三"
            )),
            Map.entry("COMBINATION-CHOSEN_3", List.of(
                    "后三码-后三组选-组六",
                    "前三码-前三组选-组六",
                    "中三码-中三组选-组六",
                    "三不同号-三不同号-标准选号",
                    "三码-三码-前三组选复式",
                    "任选复式-任选复式-三中三",
                    "三码-组选-组六",
                    "任选-任选型-任选3",
                    "不定胆-四星不定胆-四星三码不定胆",
                    "不定胆-五星不定胆-五星三码不定胆"
            )),
            Map.entry("MULTI_COMBINATION_3_ALLPOS", List.of(
                    "多位置-前中后三星组选-组三"
            )),
            Map.entry("COMBINATION-CHOSEN_3_ALLPOS", List.of(
                    "多位置-前中后三星组选-组六"
            )),
            Map.entry("SINGLE_GROUP_3", List.of(
                    "后三码-后三组选-混合组选",
                    "前三码-前三组选-混合组选",
                    "中三码-中三组选-混合组选",
                    "后三码-后三组选-混合",
                    "前三码-前三组选-混合",
                    "三码-组选-混合",
                    "中三码-中三组选-混合"
            )),
            Map.entry("SINGLE_GROUP_3_ALLPOS", List.of(
                    "多位置-前中后三星组选-混合组选"
            )),
            Map.entry("SINGLE_GROUP_3_BANCO", List.of(
                    "后三码-后三组选-不定位",
                    "三码-后三组选-后三任选"
            )),
            Map.entry("SINGLE_GROUP_2_BANCO", List.of(
                    "二码-二星组选-中二任选"
            )),
            Map.entry("SUM_PLUS_GROUP_3", List.of(
                    "后三码-后三组选-组选和值",
                    "前三码-前三组选-组选和值",
                    "中三码-中三组选-组选和值",
                    "三码-组选-组选和值",
                    "三码-组选-和值"
            )),
            Map.entry("SUM_PLUS_GROUP_3_ALLPOS", List.of(
                    "多位置-前中后三星组选-组选和值"
            )),
            Map.entry("SINGLE_2", List.of(
                    "二码-二星直选-后二直选(单式)",
                    "二码-二星直选-前二直选(单式)",
                    "二码-二星直选-中二直选(单选)",
                    "二码-二星直选-后二直选(单选)",
                    "二码-二星直选-前二直选单式",
                    "二码-二星直选-后二直选单式",
                    "二码-二星-后二直选单式",
                    "二码-二星-前二直选单式",
                    "二码-二码-后二直选单式",
                    "二星-二星-后二直选单式",
                    "二星-二星-前二直选单式",
                    "二码-后二码-单式",
                    "尾-后二-单式",
                    "一等奖-后二-单式"
            )),
            Map.entry("SUM_PLUS_DIRECT_2", List.of(
                    "二码-二星直选-后二直选和值",
                    "二码-二星直选-前二直选和值"
            )),
            Map.entry("MULTI_COMBINATION_2", List.of(
                    "二码-二星组选-后二组选(复式)",
                    "二码-二星组选-前二组选(复式)",
                    "不定胆-三星不定胆-后三二码不定胆",
                    "不定胆-三星不定胆-前三二码不定胆",
                    "二码-二星组选-前二组选复式",
                    "二码-二星组选-后二组选复式",
                    "二不同号-二不同号-标准选号",
                    "二码-二码-前二组选复式",
                    "任选复式-任选复式-二中二",
                    "二码-二星-后二组选复式",
                    "二码-二星-前二组选复式",
                    "二码-二码-后二组选复式",
                    "任选-任选型-任选2",
                    "二星-二星-后二组选复式",
                    "二星-二星-前二组选复式",
                    "不定胆-三星不定胆-中三二码不定胆",
                    "不定胆-四星不定胆-四星二码不定胆",
                    "不定胆-五星不定胆-五星二码不定胆",
                    "不定胆-不定胆-二码不定胆"
            )),
            Map.entry("SINGLE_GROUP_2", List.of(
                    "二码-二星组选-后二组选(单式)",
                    "二码-二星组选-前二组选(单式)",
                    "二码-二星组选-前二组选单式",
                    "二码-二星组选-后二组选单式",
                    "二码-二星-后二组选单式",
                    "二码-二星-前二组选单式",
                    "二码-二码-后二组选单式",
                    "二星-二星-后二组选单式",
                    "二星-二星-前二组选单式"
            )),
            Map.entry("SUM_PLUS_GROUP_2", List.of(
                    "二码-二星组选-后二组选和值",
                    "二码-二星组选-前二组选和值"
            )),
            Map.entry("ANY_3_BY_TIME1", List.of(
                    "任意码-三星任意码-后三任意码"
            )),
            Map.entry("ANY_2_BY_TIME1", List.of(
                    "任意码-二星任意码-中二任意码"
            )),
            Map.entry("TIMES_1", List.of(
                    "定位胆-定位胆-定位胆",
                    "不定胆-三星不定胆-后三一码不定胆",
                    "不定胆-三星不定胆-前三一码不定胆",
                    "大小单双-大小单双-总和",
                    "趣味-特殊-一帆风顺",
                    "趣味-特殊-好事成双",
                    "趣味-特殊-三星报喜",
                    "趣味-特殊-四季发财",
                    "和值尾数-和值尾数-前三和值尾数",
                    "和值尾数-和值尾数-中三和值尾数",
                    "和值尾数-和值尾数-后三和值尾数",
                    "龙虎-龙虎-龙虎斗",
                    "龙虎-龙虎-玄麟斗",
                    "龙虎斗-龙虎-龙虎斗",
                    "龙虎斗-龙虎-玄麟斗",
                    "百家乐-百家乐-庄闲",
                    "百家乐-百家乐-和",
                    "百家乐-百家乐-对子",
                    "百家乐-百家乐-豹子",
                    "百家乐-百家乐-天王",
                    "全5中1-全5中1-组选1",
                    "全5中1-全5中1-组选5",
                    "全5中1-全5中1-组选10",
                    "全5中1-全5中1-组选20",
                    "全5中1-全5中1-组选30",
                    "全5中1-全5中1-组选60",
                    "全5中1-全5中1-组选120",
                    "二同号复选-二同号复选-二同号复选",
                    "三同号单选-三同号单选-三同号单选",
                    "和值-和值-和值",
                    "猜冠军-猜冠军-猜冠军",
                    "定位胆-定位胆-1～5位定位胆",
                    "定位胆-定位胆-6～10位定位胆",
                    "定位胆-定位胆-1～10位定位胆",
                    "大小单双-大小单双-冠军",
                    "大小单双-大小单双-亚军",
                    "大小单双-大小单双-季军",
                    "和值-和值-冠亚和值",
                    "和值-和值-中二和值",
                    "和值-和值-后二和值",
                    "和值-三码-前三和值",
                    "和值-三码-后三和值",
                    "龙虎-龙虎-冠军龙虎",
                    "龙虎-龙虎-亚军龙虎",
                    "龙虎-龙虎-季军龙虎",
                    "龙虎-龙虎-第四名龙虎",
                    "龙虎-龙虎-第五名龙虎",
                    "独赢-独赢-独赢",
                    "位置-位置-1～5位位置",
                    "位置-位置-6～10位位置",
                    "和值-连赢-后二和值",
                    "和值-连赢-中二和值",
                    "和值-连赢-前二和值",
                    "和值-三连环-后三和值",
                    "和值-三连环-前三和值",
                    "不定胆-不定胆-前三位",
                    "趣味型-趣味型-定单双",
                    "趣味型-趣味型-猜中位",
                    "任选复式-任选复式-一中一",
                    "不定胆-不定胆-一码不定胆",
                    "趣味-趣味型-和值单双",
                    "趣味-趣味型-和值大小",
                    "趣味-趣味型-奇偶盘",
                    "趣味-趣味型-上下盘",
                    "趣味-趣味型-和值大小单双",
                    "任选-任选型-任选1",
                    "盘面-盘面-上下盘",
                    "盘面-盘面-奇偶盘",
                    "龙虎斗-龙虎-万千",
                    "龙虎斗-龙虎-万百",
                    "龙虎斗-龙虎-万十",
                    "龙虎斗-龙虎-万个",
                    "龙虎斗-龙虎-千百",
                    "龙虎斗-龙虎-千十",
                    "龙虎斗-龙虎-千个",
                    "龙虎斗-龙虎-百十",
                    "龙虎斗-龙虎-百个",
                    "龙虎斗-龙虎-十个",
                    "冠军-冠军-猜冠军",
                    "不定胆-三星不定胆-中三一码不定胆",
                    "不定胆-四星不定胆-四星一码不定胆",
                    "不定胆-五星不定胆-五星一码不定胆",
                    "大小单双-和值大小单双-五星和值",
                    "大小单双-和值大小单双-前三和值",
                    "大小单双-和值大小单双-中三和值",
                    "大小单双-和值大小单双-后三和值",
                    "大小单双-大小个数-五星大小个数",
                    "大小单双-大小个数-四星大小个数",
                    "大小单双-大小个数-前三大小个数",
                    "大小单双-大小个数-中三大小个数",
                    "大小单双-大小个数-后三大小个数",
                    "大小单双-单双个数-五星单双个数",
                    "大小单双-单双个数-四星单双个数",
                    "大小单双-单双个数-前三单双个数",
                    "大小单双-单双个数-中三单双个数",
                    "大小单双-单双个数-后三单双个数",
                    "牛牛-牛牛-牛牛",
                    "不定胆-二星不定胆-二星一码不定胆",
                    "四星套彩-四星套彩-四星套彩",
                    "鱼虾蟹-鱼虾蟹-鱼虾蟹",
                    "大小单双-大小单双组合-定位胆"
            )),
            Map.entry("ANY_CHOSEN_DIRECT_2", List.of(
                    "任选二-任二直选-直选复式"
            )),
            Map.entry("ANY_SINGLE_DIRECT_2", List.of(
                    "任选二-任二直选-直选单式"
            )),
            Map.entry("ANY_CHOSEN_DIRECT_2_SUM_PLUS", List.of(
                    "任选二-任二直选-直选和值"
            )),
            Map.entry("ANY_CHOSEN_GROUP_2", List.of(
                    "任选二-任二组选-组选复式"
            )),
            Map.entry("ANY_CHOSEN_SINGLE_2", List.of(
                    "任选二-任二组选-组选单式"
            )),
            Map.entry("ANY_SUM_PLUS_GROUP_2", List.of(
                    "任选二-任二组选-组选和值"
            )),
            Map.entry("ANY_CHOSEN_DIRECT_3", List.of(
                    "任选三-任三直选-直选复式"
            )),
            Map.entry("SINGLE_DIRECT_3_POSITION", List.of(
                    "任选三-任三直选-直选单式"
            )),
            Map.entry("ANY_CHOSEN_DIRECT_3_SUM_PLUS", List.of(
                    "任选三-任三直选-直选和值"
            )),
            Map.entry("ANY_MULTI_COMBINATION_3", List.of(
                    "任选三-任三组选-组三"
            )),
            Map.entry("ANY_COMBINATION-CHOSEN_3", List.of(
                    "任选三-任三组选-组六"
            )),
            Map.entry("ANY_SINGLE_GROUP_3", List.of(
                    "任选三-任三组选-混合组选"
            )),
            Map.entry("ANY_SUM_PLUS_GROUP_3", List.of(
                    "任选三-任三组选-组选和值"
            )),
            Map.entry("ANY_CHOSEN_DIRECT_4", List.of(
                    "任选四-任四直选-直选复式"
            )),
            Map.entry("SINGLE_DIRECT_4_POSITION", List.of(
                    "任选四-任四直选-直选单式"
            )),
            Map.entry("ANY_CHOSEN_GROUP_4", List.of(
                    "任选四-任四组选-组选24"
            )),
            Map.entry("MULTI_COMBINATION_POSITION", List.of(
                    "任选四-任四组选-组选12",
                    "任选四-任四组选-组选6",
                    "任选四-任四组选-组选4"
            )),
            Map.entry("SPAN_2", List.of(
                    "直选跨度-二码跨度-前二跨度",
                    "直选跨度-二码跨度-后二跨度"
            )),
            Map.entry("SPAN_3", List.of(
                    "直选跨度-三码跨度-前三跨度",
                    "直选跨度-三码跨度-中三跨度",
                    "直选跨度-三码跨度-后三跨度"
            )),
            Map.entry("INCLUDE_3", List.of(
                    "组选包胆-组选包胆-前三包胆",
                    "组选包胆-组选包胆-中三包胆",
                    "组选包胆-组选包胆-后三包胆"
            )),
            Map.entry("SHOUDONG_SINGLE_2", List.of(
                    "二不同号-二不同号-手动输入"
            )),
            Map.entry("SHOUDONG_SAME_SINGLE_3", List.of(
                    "二同号单选-二同号单选-手动输入"
            )),
            Map.entry("SHOUDONG_SINGLE_3", List.of(
                    "三不同号-三不同号-手动输入"
            )),
            Map.entry("HEZHI", List.of(
                    "三不同号-三不同号-和值选号"
            )),
            Map.entry("GENERAL_ELECTION", List.of(
                    "三同号通选-三同号通选-三同号通选",
                    "三连号通选-三连号通选-三连号通选"
            )),
            Map.entry("RANKING", List.of(
                    "猜前二-猜前二-复式",
                    "猜前三-猜前三-复式",
                    "猜前四-猜前四-复式",
                    "猜前五-猜前五-复式",
                    "猜前六-猜前六-复式",
                    "连赢-连赢-复式",
                    "三连环-三连环-复式",
                    "四连环-四连环-复式",
                    "五连环-五连环-复式",
                    "六连环-六连环-复式",
                    "三码-三码-前三直选复式",
                    "二码-二码-前二直选复式",
                    "前二-猜前二-复式",
                    "前三-猜前三-复式",
                    "前四-猜前四-复式",
                    "前五-猜前五-复式",
                    "前六-猜前六-复式"
            )),
            Map.entry("RANKING_SINGLE_2", List.of(
                    "猜前二-猜前二-单式",
                    "连赢-连赢-单式",
                    "二码-二码-前二直选单式",
                    "前二-猜前二-单式"
            )),
            Map.entry("RANKING_SINGLE_3", List.of(
                    "猜前三-猜前三-单式",
                    "三连环-三连环-单式",
                    "三码-三码-前三直选单式",
                    "前三-猜前三-单式"
            )),
            Map.entry("RANKING_SINGLE_4", List.of(
                    "猜前四-猜前四-单式",
                    "四连环-四连环-单式",
                    "前四-猜前四-单式"
            )),
            Map.entry("RANKING_SINGLE_5", List.of(
                    "猜前五-猜前五-单式",
                    "五连环-五连环-单式",
                    "前五-猜前五-单式"
            )),
            Map.entry("RANKING_SINGLE_6", List.of(
                    "猜前六-猜前六-单式",
                    "六连环-六连环-单式",
                    "前六-猜前六-单式"
            )),
            Map.entry("RANKING_PK", List.of(
                    "竞速-竞速-竞速",
                    "对决-对决-对决"
            )),
            Map.entry("RANKING_SINGLE_GROUP_3", List.of(
                    "三码-三码-前三组选单式",
                    "任选单式-任选单式-三中三"
            )),
            Map.entry("RANKING_SINGLE_GROUP_2", List.of(
                    "二码-二码-前二组选单式",
                    "任选单式-任选单式-二中二"
            )),
            Map.entry("COMBINATION-CHOSEN_4", List.of(
                    "任选复式-任选复式-四中四",
                    "任选-任选型-任选4"
            )),
            Map.entry("COMBINATION-CHOSEN_5", List.of(
                    "任选复式-任选复式-五中五",
                    "任选-任选型-任选5"
            )),
            Map.entry("COMBINATION-CHOSEN_6", List.of(
                    "任选复式-任选复式-六中五",
                    "任选-任选型-任选6"
            )),
            Map.entry("COMBINATION-CHOSEN_7", List.of(
                    "任选复式-任选复式-七中五",
                    "任选-任选型-任选7"
            )),
            Map.entry("COMBINATION-CHOSEN_8", List.of(
                    "任选复式-任选复式-八中五"
            )),
            Map.entry("NUM_IN_NUM_1", List.of(
                    "任选单式-任选单式-一中一"
            )),
            Map.entry("RANKING_SINGLE_GROUP_4", List.of(
                    "任选单式-任选单式-四中四"
            )),
            Map.entry("RANKING_SINGLE_GROUP_5", List.of(
                    "任选单式-任选单式-五中五"
            )),
            Map.entry("RANKING_SINGLE_GROUP_6", List.of(
                    "任选单式-任选单式-六中五"
            )),
            Map.entry("NUM_IN_NUM_7", List.of(
                    "任选单式-任选单式-七中五"
            )),
            Map.entry("NUM_IN_NUM_8", List.of(
                    "任选单式-任选单式-八中五"
            )),
            Map.entry("PICK_GROUP_2", List.of(
                    "包组-后二直选-复式"
            )),
            Map.entry("PICK_GROUP_2_SINGLE", List.of(
                    "包组-后二直选-单式"
            )),
            Map.entry("PICK_GROUP_3", List.of(
                    "包组-后三直选-复式"
            )),
            Map.entry("PICK_GROUP_3_SINGLE", List.of(
                    "包组-后三直选-单式"
            )),
            Map.entry("PICK_GROUP_4", List.of(
                    "包组-后四直选-复式"
            )),
            Map.entry("PICK_GROUP_4_SINGLE", List.of(
                    "包组-后四直选-单式"
            )),
            Map.entry("HEAD", List.of(
                    "头-头-复式"
            )),
            Map.entry("HEAD_SINGLE", List.of(
                    "头-头-单式"
            )),
            Map.entry("HEAD_FOOTER", List.of(
                    "头与尾-头与尾-复式"
            )),
            Map.entry("HEAD_FOOTER_SINGLE", List.of(
                    "头与尾-头与尾-单式"
            )),
            Map.entry("LINE_MISS_2", List.of(
                    "串号-选2-复式"
            )),
            Map.entry("LINE_MISS_2_SINGLE", List.of(
                    "串号-选2-单式"
            )),
            Map.entry("LINE_MISS_3", List.of(
                    "串号-选3-复式"
            )),
            Map.entry("LINE_MISS_3_SINGLE", List.of(
                    "串号-选3-单式"
            )),
            Map.entry("LINE_MISS_4", List.of(
                    "串号-选4-复式",
                    "不中-4号不中-复式"
            )),
            Map.entry("LINE_MISS_4_SINGLE", List.of(
                    "串号-选4-单式",
                    "不中-4号不中-单式"
            )),
            Map.entry("LINE_MISS_8", List.of(
                    "不中-8号不中-复式"
            )),
            Map.entry("LINE_MISS_8_SINGLE", List.of(
                    "不中-8号不中-单式"
            )),
            Map.entry("LINE_MISS_10", List.of(
                    "不中-10号不中-复式"
            )),
            Map.entry("LINE_MISS_10_SINGLE", List.of(
                    "不中-10号不中-单式"
            )),
            Map.entry("ANY_CHOSEN_DANTUO", List.of(
                    "任选胆拖-任选胆拖-二中二", "任选胆拖-任选胆拖-三中三", "任选胆拖-任选胆拖-四中四", "任选胆拖-任选胆拖-五中五", "任选胆拖-任选胆拖-六中五", "任选胆拖-任选胆拖-七中五", "任选胆拖-任选胆拖-八中五"
            ))
    );

    public static final Map<String, List<String>> RULES = Map.ofEntries(
            Map.entry("MULTI", List.of("multi")),
            Map.entry("COMBINATION", List.of("combination")),
            Map.entry("COMBINATION-CHOSEN_3", List.of("add-num-3", "combination-chosen")),
            Map.entry("COMBINATION-CHOSEN_4", List.of("add-num-4", "combination-chosen")),
            Map.entry("COMBINATION-CHOSEN_5", List.of("add-num-5", "combination-chosen")),
            Map.entry("COMBINATION-CHOSEN_6", List.of("add-num-6", "combination-chosen")),
            Map.entry("COMBINATION-CHOSEN_7", List.of("add-num-7", "combination-chosen")),
            Map.entry("COMBINATION-CHOSEN_8", List.of("add-num-8", "combination-chosen")),
            Map.entry("COMBINATION-CHOSEN_3_ALLPOS", List.of("add-num-3", "combination-chosen", "multiply-3")),
            Map.entry("ANY_COMBINATION-CHOSEN_3", List.of("add-num-3", "combination-chosen", "position-chosen", "add-posnum-3")),
            Map.entry("MULTI_COMBINATION_3", List.of("add-num-2", "combination-chosen", "times-chosen")),
            Map.entry("MULTI_COMBINATION_3_ALLPOS", List.of("add-num-2", "combination-chosen", "times-chosen", "multiply-3")),
            Map.entry("ANY_MULTI_COMBINATION_3", List.of("add-num-2", "combination-chosen", "add-posnum-3", "times-chosen", "position-chosen")),
            Map.entry("MULTI_COMBINATION", List.of("multi-combination")),
            Map.entry("MULTI_COMBINATION_2", List.of("add-num-2", "combination-chosen")),
            Map.entry("MULTI_COMBINATION_POSITION", List.of("multi-combination", "add-posnum-4", "position-chosen")),
            Map.entry("SINGLE_5", List.of("add-num-5", "single")),
            Map.entry("SINGLE_GROUP_3", List.of("add-num-3", "add-flag-group", "single")),
            Map.entry("SINGLE_GROUP_3_ALLPOS", List.of("add-num-3", "add-flag-group", "single", "multiply-3")),
            Map.entry("SINGLE_GROUP_3_BANCO", List.of("add-num-3", "add-flag-group", "add-flag-banco", "single")),
            Map.entry("SINGLE_GROUP_2_BANCO", List.of("add-num-2", "add-flag-group", "add-flag-banco", "single")),
            Map.entry("SINGLE_GROUP_2", List.of("add-num-2", "add-flag-group", "single")),
            Map.entry("ANY_SINGLE_GROUP_3", List.of("add-num-3", "add-flag-group", "single", "add-posnum-3", "position-chosen")),
            Map.entry("SUM_PLUS_DIRECT_3", List.of("add-flag-direct", "add-num-3", "sum-plus")),
            Map.entry("SUM_PLUS_DIRECT_2", List.of("add-flag-direct", "add-num-2", "sum-plus")),
            Map.entry("SUM_PLUS_GROUP_3", List.of("add-flag-group", "add-num-3", "sum-plus")),
            Map.entry("SUM_PLUS_GROUP_3_ALLPOS", List.of("add-flag-group", "add-num-3", "sum-plus", "multiply-3")),
            Map.entry("ANY_SUM_PLUS_GROUP_3", List.of("add-flag-group", "add-num-3", "sum-plus", "add-posnum-3", "position-chosen")),
            Map.entry("SUM_PLUS_GROUP_2", List.of("add-flag-group", "add-num-2", "sum-plus")),
            Map.entry("ANY_SUM_PLUS_GROUP_2", List.of("add-flag-group", "add-num-2", "sum-plus", "add-posnum-2", "position-chosen")),
            Map.entry("ANY_CHOSEN_GROUP_4", List.of("add-flag-group", "add-num-4", "combination-chosen", "position-chosen")),
            Map.entry("ANY_SINGLE_DIRECT_2", List.of("add-num-2", "add-flag-direct", "single", "position-chosen")),
            Map.entry("ANY_CHOSEN_DIRECT_2", List.of("add-num-2", "any-chosen", "any-solo")),
            Map.entry("ANY_CHOSEN_GROUP_2", List.of("add-num-2", "combination-chosen", "position-chosen")),
            Map.entry("ANY_CHOSEN_DIRECT_3", List.of("add-num-3", "any-chosen", "any-solo")),
            Map.entry("ANY_CHOSEN_DIRECT_4", List.of("add-num-4", "any-chosen", "any-solo")),
            Map.entry("ANY_3_BY_TIME1", List.of("times-chosen")),
            Map.entry("ANY_2_BY_TIME1", List.of("times-chosen")),
            Map.entry("TIMES_1", List.of("times-chosen")),
            Map.entry("ANY_CHOSEN_DIRECT_2_SUM_PLUS", List.of("add-num-2", "add-flag-direct", "sum-plus", "add-posnum-2", "position-chosen")),
            Map.entry("ANY_CHOSEN_DIRECT_3_SUM_PLUS", List.of("add-num-3", "add-flag-direct", "sum-plus", "add-posnum-3", "position-chosen")),
            Map.entry("SPAN_2", List.of("add-num-2", "span-chosen")),
            Map.entry("SPAN_3", List.of("add-num-3", "span-chosen")),
            Map.entry("INCLUDE_3", List.of("add-num-54", "times-chosen")),
            Map.entry("SINGLE_DIRECT_3_POSITION", List.of("add-num-3", "add-flag-direct", "single", "position-chosen")),
            Map.entry("SINGLE_DIRECT_4_POSITION", List.of("add-num-4", "add-flag-direct", "single", "position-chosen")),
            Map.entry("SINGLE_4", List.of("add-num-4", "single")),
            Map.entry("SINGLE_3", List.of("add-num-3", "single")),
            Map.entry("SINGLE_2", List.of("add-num-2", "single")),
            Map.entry("ANY_CHOSEN_SINGLE_2", List.of("add-num-2", "single", "add-posnum-2", "position-chosen", "add-flag-group")),
            Map.entry("RANKING", List.of("ranking-chosen")),
            Map.entry("RANKING_SINGLE_2", List.of("add-num-1", "single")),
            Map.entry("RANKING_SINGLE_GROUP_2", List.of("add-num-1", "single", "add-flag-group")),
            Map.entry("RANKING_SINGLE_3", List.of("add-num-2", "single")),
            Map.entry("RANKING_SINGLE_GROUP_3", List.of("add-num-2", "single", "add-flag-group")),
            Map.entry("RANKING_SINGLE_4", List.of("add-num-3", "single")),
            Map.entry("RANKING_SINGLE_GROUP_4", List.of("add-num-3", "single", "add-flag-group")),
            Map.entry("RANKING_SINGLE_5", List.of("add-num-4", "single")),
            Map.entry("RANKING_SINGLE_GROUP_5", List.of("add-num-4", "single", "add-flag-group")),
            Map.entry("RANKING_SINGLE_6", List.of("add-num-5", "single")),
            Map.entry("RANKING_SINGLE_GROUP_6", List.of("add-num-5", "single", "add-flag-group")),
            Map.entry("NUM_IN_NUM_1", List.of("add-num-0", "single")),
            Map.entry("NUM_IN_NUM_7", List.of("add-num-6", "single", "add-flag-group")),
            Map.entry("NUM_IN_NUM_8", List.of("add-num-7", "single", "add-flag-group")),
            Map.entry("HEZHI", List.of("add-num-3", "add-flag-group", "sum-plus-dif")),
            Map.entry("SHOUDONG_SINGLE_3", List.of("add-num-3", "single")),
            Map.entry("SHOUDONG_SINGLE_2", List.of("add-num-2", "single")),
            Map.entry("SHOUDONG_SAME_SINGLE_3", List.of("add-num-3", "single", "add-flag-group")),
            Map.entry("RANKING_PK", List.of("without-format-codes", "ranking-pk")),
            Map.entry("DANTUO_DIF_3", List.of("dantuo-dif-3")),
            Map.entry("DANTUO_DIF_2", List.of("dantuo-dif-2")),
            Map.entry("GENERAL_ELECTION", List.of("general-election")),
            Map.entry("PICK_GROUP_2", List.of("add-num-27", "multi", "times-chosen")),
            Map.entry("PICK_GROUP_2_SINGLE", List.of("add-num-2", "single", "add-bet-num-27")),
            Map.entry("PICK_GROUP_3", List.of("add-num-23", "multi", "times-chosen")),
            Map.entry("PICK_GROUP_3_SINGLE", List.of("add-num-3", "single", "add-bet-num-23")),
            Map.entry("PICK_GROUP_4", List.of("add-num-20", "multi", "times-chosen")),
            Map.entry("PICK_GROUP_4_SINGLE", List.of("add-num-4", "single", "add-bet-num-20")),
            Map.entry("HEAD", List.of("add-num-4", "multi", "times-chosen")),
            Map.entry("HEAD_SINGLE", List.of("add-num-2", "single", "add-bet-num-4")),
            Map.entry("HEAD_FOOTER", List.of("add-num-5", "multi", "times-chosen")),
            Map.entry("HEAD_FOOTER_SINGLE", List.of("add-num-2", "single", "add-bet-num-5")),
            Map.entry("LINE_MISS_2", List.of("line-miss-2")),
            Map.entry("LINE_MISS_2_SINGLE", List.of("add-num-4", "single")),
            Map.entry("LINE_MISS_3", List.of("line-miss-3")),
            Map.entry("LINE_MISS_3_SINGLE", List.of("add-num-6", "single")),
            Map.entry("LINE_MISS_4", List.of("line-miss-4")),
            Map.entry("LINE_MISS_4_SINGLE", List.of("add-num-8", "single")),
            Map.entry("LINE_MISS_8", List.of("line-miss-8")),
            Map.entry("LINE_MISS_8_SINGLE", List.of("add-num-16", "single")),
            Map.entry("LINE_MISS_10", List.of("line-miss-10")),
            Map.entry("LINE_MISS_10_SINGLE", List.of("add-num-20", "single")),
            Map.entry("ANY_CHOSEN_DANTUO", List.of("dantuo-any"))
    );

    public static final Map<String, Integer> mapMatchNameToNumList = Map.ofEntries(
            Map.entry("包组-后二直选", 18),
            Map.entry("包组-后三直选", 17),
            Map.entry("包组-后四直选", 16),
            Map.entry("头-头", 1),
            Map.entry("头与尾-头与尾", 2)
    );

    // 时时彩部分
    public static final List<String> sscAlias = Arrays.asList(
            "cqssc", "hn1fc", "hn5fc", "xjssc", "tjssc", "jnd30m", "rd1fc", "rd2fc", "txffc", "qqffc",
            "rbffc", "xgffc", "ynffc", "cq30s", "tx30s", "mmc", "txdsq", "txssq", "txwfc", "eosffc",
            "jdcqssc", "sytxffc", "cqffc", "cq5fc", "tg60s", "tg300s", "qq30m", "xgwfc", "ynwfc",
            "qq1fc", "syqiqu", "azxy5", "zfbffc", "btcffc", "zfb5fc", "hntc", "lztc", "mltc", "tgfctc",
            "gsjp", "gskr", "gshk", "gscn", "gstw", "gssg", "gseg", "gsde", "gsgb", "gsru", "gsin",
            "gsus", "gsth", "ynhn", "pl5", "tlzc", "hnvip", "ynma", "gsb3y", "gsb5y", "baac",
            "bnbtcffc", "bnbtcwfc", "bnethffc", "bnethwfc", "okbtcffc", "okbtcwfc", "okethffc",
            "okethwfc", "hashffc", "hashsfc", "hashwfc"
    );

    // 11选5部分
    public static final List<String> _11x5Alias = Arrays.asList(
            "gd11x5", "jx11x5", "sd11x5", "sh11x5", "ah11x5", "jnd11x5", "tg11x5", "js11x5",
            "gx11x5", "hlj11x5", "hb11x5", "ln11x5", "tg11x5ff", "yn11x5"
    );

    // 3D 排列3
    public static final List<String> _3dAlias = Arrays.asList("3dfc", "pl3", "3djnd");

    // PK10
    public static final List<String> pk10Alias = Arrays.asList(
            "pk10", "xyft", "ydl10", "jssc", "ynpk10ff", "ynpk10wf", "azxy10"
    );

    // jssm
    public static final List<String> jssmAlias = Arrays.asList("jssm");

    // 快3
    public static final List<String> k3Alias = Arrays.asList("jsk3", "ahk3", "gxk3", "hbk3", "hnks", "jlks");
    //六合彩
    public static final List<String> lhcAlias = Arrays.asList("xglhc", "amlhc");

    // vnm - 北部
    public static final List<String> vnmNorAlias = Arrays.asList("vnn");

    // vnm - 中部, 南部
    public static final List<String> vnmMidSouAlias = Arrays.asList(
            "sfc", "fac", "dolc", "gnc", "xxc", "qhc", "pdc", "gzc", "gpc", "jlc", "lsc", "gyc",
            "dnc", "kgc", "fzmsc", "joc", "ttc", "blc", "bzc", "tdc", "tnc", "jyc", "szc", "ajc",
            "xlc", "psc", "pyc", "ylc", "crc", "pfc", "lac", "hjc", "qjc", "jjc", "dlc"
    );

    // vnm - 极速
    public static final List<String> vnmFastAlias = Arrays.asList("vnffc", "vnwfc");

    // 类型和别名映射
    public static final Map<String, List<String>> lotteryAliasMap = Map.of(
            "ssc", sscAlias,
            "11x5", _11x5Alias,
            "3d", _3dAlias,
            "pk10", pk10Alias,
            "jssm", jssmAlias,
            "k3", k3Alias,
            "vnmNorth", vnmNorAlias,
            "vnmMidSouth", vnmMidSouAlias,
            "vnmFast", vnmFastAlias
    );

    /**
     * 玩法对规则指纹的匹配关系(除去单式)
     *
     * @param {[type]} mathcherName [玩法匹配名称]
     * @return {[type]}              [玩法指纹]
     */
    public static String method2Token(String matcherName) {
        for (Map.Entry<String, List<String>> entry : TOKENS.entrySet()) {
            if (entry.getValue().contains(matcherName)) {
                return entry.getKey();
            }
        }
        return null; // 如果没有找到匹配项，返回 null
    }

    /**
     * 玩法指纹对规则项的匹配关系
     *
     * @param {[type]} token [玩法指纹]
     * @return {[type]}       [规则项]
     */
    public static List<String> token2Rules(String token) {
        return RULES.get(token);
    }

    /**
     * 六合彩规则
     *
     * @param {[type]} lotteryId [彩种ID]
     * @return {[type]}           [description]
     */
    public static List<String> mathLhcRules(int lotteryId) {
        List<String> ruleSuite = new ArrayList<>();

        // 根据彩种ID进行判断
        switch (lotteryId) {
            case 73:
            case 20:
                ruleSuite.add("lhc-common");
                break;
            // 你可以在这里添加更多的 case 语句来处理其他彩种ID
        }

        // 返回结果
        return ruleSuite;
    }
}
