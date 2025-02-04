package com.xtree.lottery.rule.recent;

import com.xtree.lottery.rule.betting.Matchers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecentMatchers {
    private static Map<String, String> map = new HashMap<>();

    //public static Map<String, String> getMap() {
    //    map.put("c_7c73ff", "c_7c73ff");
    //    map.put("c_229e6d", "c_229e6d");
    //    map.put("c_cc8b1e", "c_cc8b1e");
    //    map.put("c_006dfe", "c_006dfe");
    //    map.put("c_ff33ff", "c_ff33ff");
    //    map.put("c_ff0000", "c_ff0000");
    //    map.put("b_f8aa46", "ball b_f8aa46");
    //    map.put("b_4c8bda", "ball b_4c8bda");
    //    map.put("b_008000", "ball b_008000");
    //    map.put("b_ff0000", "ball b_ff0000");
    //    map.put("c_f95016", "c_f95016");
    //    map.put("c_476efe", "c_476efe");
    //    map.put("c_fc5d50", "c_fc5d50");
    //    map.put("c_1291bb", "c_1291bb");
    //    map.put("c_0b9fb9", "c_0b9fb9");
    //    map.put("c_685ba2", "c_685ba2");
    //    return map;
    //}

    public static List<String> method2RuleSuite(String methodName, String alias) {
        List<String> result = new ArrayList<>();

        // 如果 alias 在 sscAlias 列表中
        if (Matchers.sscAlias.contains(alias)) {
            switch (methodName) {
                case "五星复式":
                case "五星单式":
                case "五星组合":
                case "五星组选120":
                case "五星组选60":
                case "五星组选30":
                case "五星组选20":
                case "五星组选10":
                case "五星组选5":
                case "趣味一帆风顺":
                case "趣味好事成双":
                case "趣味三星报喜":
                case "趣味四季发财":
                    result = new ArrayList<>();
                    result.add("X,X,X,X,X");
                    result.add("GROUP");
                    break;
                case "四星复式":
                case "四星单式":
                case "四星组合":
                case "四星组选24":
                case "四星组选12":
                case "四星组选6":
                case "四星组选4":
                    result = new ArrayList<>();
                    result.add("-,X,X,X,X");
                    result.add("GROUP");
                    break;
                case "后三码复式":
                case "后三码单式":
                case "后三码组三":
                case "后三码组六":
                case "后三码混合":
                case "后三码混合组选":
                case "后三码直选复式":
                case "后三码直选单式":
                case "组选包胆后三包胆":
                    result = new ArrayList<>();
                    result.add("-,-,X,X,X");
                    result.add("GROUP");
                    break;
                case "后三码直选和值":
                case "后三码组选和值":
                    result = new ArrayList<>();
                    result.add("-,-,X,X,X");
                    result.add("SUM");
                    break;
                case "前三码复式":
                case "前三码直选复式":
                case "前三码单式":
                case "前三码直选单式":
                case "前三码组三":
                case "前三码组六":
                case "前三码混合组选":
                case "前三码混合":
                case "组选包胆前三包胆":
                    result = new ArrayList<>();
                    result.add("X,X,X,-,-");
                    result.add("GROUP");
                    break;
                case "前三码直选和值":
                case "前三码组选和值":
                    result = new ArrayList<>();
                    result.add("X,X,X,-,-");
                    result.add("SUM");
                    break;
                case "中三码复式":
                case "中三码单式":
                case "中三码组三":
                case "中三码组六":
                case "中三码混合组选":
                case "组选包胆中三包胆":
                    result = new ArrayList<>();
                    result.add("-,X,X,X,-");
                    result.add("GROUP");
                    break;
                case "中三码直选和值":
                case "中三码组选和值":
                    result = new ArrayList<>();
                    result.add("-,X,X,X,-");
                    result.add("SUM");
                    break;
                case "二码后二直选(复式)":
                case "二码后二组选(复式)":
                case "二码后二直选(单式)":
                case "二码后二组选(单式)":
                case "二码后二直选复式":
                case "二码后二组选复式":
                case "二码后二直选单式":
                case "二码后二组选单式":
                case "二码后二直选和值":
                case "二码后二组选和值":
                    result = new ArrayList<>();
                    result.add("-,-,-,X,X");
                    result.add("SUM");
                    break;
                case "二码前二直选(复式)":
                case "二码前二组选(复式)":
                case "二码前二直选(单式)":
                case "二码前二组选(单式)":
                case "二码前二直选复式":
                case "二码前二组选复式":
                case "二码前二直选单式":
                case "二码前二组选单式":
                case "二码前二直选和值":
                case "二码前二组选和值":
                    result = new ArrayList<>();
                    result.add("-,-,-,X,X");
                    result.add("SUM");
                    break;
                case "大小单双五星和值":
                case "大小单双五星大小个数":
                case "大小单双五星单双个数":
                case "大小单双总和":
                    result = new ArrayList<>();
                    result.add("X,X,X,X,X");
                    result.add("SINGLE_DOUBLE");
                    break;
                case "大小单双前二":
                    result = new ArrayList<>();
                    result.add("X,X,-,-,-");
                    result.add("SINGLE_DOUBLE");
                    break;
                case "大小单双后二":
                    result = new ArrayList<>();
                    result.add("-,-,-,X,X");
                    result.add("SINGLE_DOUBLE");
                    break;
                case "大小单双前三和值":
                case "大小单双前三大小个数":
                case "大小单双前三单双个数":
                case "大小单双前三":
                    result = new ArrayList<>();
                    result.add("X,X,X,-,-");
                    result.add("SINGLE_DOUBLE");
                    break;
                case "大小单双后三和值":
                case "大小单双后三大小个数":
                case "大小单双后三单双个数":
                case "大小单双后三":
                    result = new ArrayList<>();
                    result.add("-,-,X,X,X");
                    result.add("SINGLE_DOUBLE");
                    break;
                case "大小单双中三和值":
                case "大小单双中三大小个数":
                case "大小单双中三单双个数":
                    result = new ArrayList<>();
                    result.add("-,X,X,X,-");
                    result.add("SINGLE_DOUBLE");
                    break;
                case "大小单双四星大小个数":
                case "大小单双四星单双个数":
                    result = new ArrayList<>();
                    result.add("-,X,X,X,X");
                    result.add("SINGLE_DOUBLE");
                    break;
                case "直选跨度前二跨度":
                    result = new ArrayList<>();
                    result.add("X,X,-,-,-");
                    result.add("SPAN");
                    break;
                case "直选跨度后二跨度":
                    result = new ArrayList<>();
                    result.add("-,-,-,X,X");
                    result.add("SPAN");
                    break;
                case "直选跨度前三跨度":
                    result = new ArrayList<>();
                    result.add("X,X,X,-,-");
                    result.add("SPAN");
                    break;
                case "直选跨度中三跨度":
                    result = new ArrayList<>();
                    result.add("-,X,X,X,-");
                    result.add("SPAN");
                    break;
                case "直选跨度后三跨度":
                    result = new ArrayList<>();
                    result.add("-,-,X,X,X");
                    result.add("SPAN");
                    break;
                case "和值尾数前三和值尾数":
                    result = new ArrayList<>();
                    result.add("X,X,X,-,-");
                    result.add("SUM_TAIL_NUM");
                    break;
                case "和值尾数中三和值尾数":
                    result = new ArrayList<>();
                    result.add("-,X,X,X,-");
                    result.add("SUM_TAIL_NUM");
                    break;
                case "和值尾数后三和值尾数":
                    result = new ArrayList<>();
                    result.add("-,-,X,X,X");
                    result.add("SUM_TAIL_NUM");
                    break;
                case "龙虎斗龙虎斗":
                    result = new ArrayList<>();
                    result.add("X,-,-,-,X");
                    result.add("DRAGON_TIGER_PK");
                    break;
                case "龙虎斗玄麟斗":
                    result = new ArrayList<>();
                    result.add("-,X,-,X,-");
                    result.add("DRAGON_TIGER_PK");
                    break;
                case "龙虎斗万千":
                    result = new ArrayList<>();
                    result.add("X,X,-,-,-");
                    result.add("DRAGON_TIGER_PK");
                    break;
                case "龙虎斗万百":
                    result = new ArrayList<>();
                    result.add("X,-,X,-,-");
                    result.add("DRAGON_TIGER_PK");
                    break;
                case "龙虎斗万十":
                    result = new ArrayList<>();
                    result.add("X,-,-,X,-");
                    result.add("DRAGON_TIGER_PK");
                    break;
                case "龙虎斗万个":
                    result = new ArrayList<>();
                    result.add("X,-,-,-,X");
                    result.add("DRAGON_TIGER_PK");
                    break;
                case "龙虎斗千百":
                    result = new ArrayList<>();
                    result.add("-,X,X,-,-");
                    result.add("DRAGON_TIGER_PK");
                    break;
                case "龙虎斗千十":
                    result = new ArrayList<>();
                    result.add("-,X,-,X,-");
                    result.add("DRAGON_TIGER_PK");
                    break;
                case "龙虎斗千个":
                    result = new ArrayList<>();
                    result.add("-,X,-,-,X");
                    result.add("DRAGON_TIGER_PK");
                    break;
                case "龙虎斗百十":
                    result = new ArrayList<>();
                    result.add("-,-,X,X,-");
                    result.add("DRAGON_TIGER_PK");
                    break;
                case "龙虎斗百个":
                    result = new ArrayList<>();
                    result.add("-,-,X,-,X");
                    result.add("DRAGON_TIGER_PK");
                    break;
                case "龙虎斗十个":
                    result = new ArrayList<>();
                    result.add("-,-,-,X,X");
                    result.add("DRAGON_TIGER_PK");
                    break;
                case "百家乐庄闲":
                case "百家乐和":
                    result = new ArrayList<>();
                    result.add("X,X,-,X,X");
                    result.add("BACCARAT_1");
                    break;
                case "百家乐对子":
                    result = new ArrayList<>();
                    result.add("X,X,-,X,X");
                    result.add("BACCARAT_2");
                    break;
                case "百家乐豹子":
                    result = new ArrayList<>();
                    result.add("X,X,X,X,X");
                    result.add("BACCARAT_3");
                    break;
                case "百家乐天王":
                    result = new ArrayList<>();
                    result.add("X,X,-,X,X");
                    result.add("BACCARAT_4");
                    break;
                case "全5中1组选1":
                case "全5中1组选5":
                case "全5中1组选10":
                case "全5中1组选20":
                case "全5中1组选30":
                case "全5中1组选60":
                case "全5中1组选120":
                    result = new ArrayList<>();
                    result.add("X,X,X,X,X");
                    result.add("5IN1");
                    break;
                case "不定胆后三一码不定胆":
                case "不定胆后三二码不定胆":
                    result = new ArrayList<>();
                    result.add("-,-,X,X,X");
                    result.add("");
                    break;
                case "不定胆前三一码不定胆":
                case "不定胆前三二码不定胆":
                    result = new ArrayList<>();
                    result.add("X,X,X,-,-");
                    result.add("");
                    break;
                case "不定胆中三一码不定胆":
                case "不定胆中三二码不定胆":
                    result = new ArrayList<>();
                    result.add("-,X,X,X,-");
                    result.add("");
                    break;
                case "不定胆四星一码不定胆":
                case "不定胆四星二码不定胆":
                case "不定胆四星三码不定胆":
                    result = new ArrayList<>();
                    result.add("-,X,X,X,X");
                    result.add("");
                    break;
                case "牛牛牛牛":
                    result = new ArrayList<>();
                    result.add("X,X,X,X,X");
                    result.add("NIUNIU");
                    break;
            }
        }

        if (Matchers.k3Alias.contains(alias)) {
            switch (methodName) {
                case "二不同号标准选号":
                case "二不同号手动输入":
                case "二同号单选标准选号":
                case "二同号单选手动输入":
                case "二同号复选二同号复选":
                case "三不同号标准选号":
                case "三不同号手动输入":
                case "三不同号和值选号":
                case "三同号单选三同号单选":
                case "三同号通选三同号通选":
                    result = new ArrayList<>();
                    result.add("X,X,X");
                    result.add("K3_GENERAL");
                    break;
                case "三连号通选三连号通选":
                    result = new ArrayList<>();
                    result.add("X,X,X");
                    result.add("K3_SERIAL_NO");
                    break;
                case "和值和值":
                    result = new ArrayList<>();
                    result.add("X,X,X");
                    result.add("SUM");
                    break;
            }
        }

        if (Matchers._3dAlias.contains(alias)) {
            switch (methodName) {
                case "二码后二直选复式":
                case "二码后二直选单式":
                case "二码后二组选复式":
                case "二码后二组选单式":
                case "二星后二直选复式":
                case "二星后二直选单式":
                case "二星后二组选复式":
                case "二星后二组选单式":
                    result = new ArrayList<>();
                    result.add("-,X,X");
                    result.add("");
                    break;
                case "二码前二直选复式":
                case "二码前二直选单式":
                case "二码前二组选复式":
                case "二码前二组选单式":
                case "二星前二直选复式":
                case "二星前二直选单式":
                case "二星前二组选复式":
                case "二星前二组选单式":
                    result = new ArrayList<>();
                    result.add("X,X,-");
                    result.add("");
                    break;
                case "三码复式":
                case "三码单式":
                case "三码组三":
                case "三码组六":
                case "三码混合":
                    result = new ArrayList<>();
                    result.add("X,X,X");
                    result.add("GROUP");
                    break;
                case "三码和值":
                case "三码组选和值":
                case "三码直选和值":
                    result = new ArrayList<>();
                    result.add("X,X,X");
                    result.add("SUM");
                    break;
            }
        }

        if (Matchers.pk10Alias.contains(alias) || Matchers.jssmAlias.contains(alias)) {
            switch (methodName) {
                case "大小单双冠军":
                    result = new ArrayList<>();
                    result.add("X,-,-,-,-,-,-,-,-,-");
                    result.add("PK10_SINGLE_DOUBLE");
                    break;
                case "大小单双亚军":
                    result = new ArrayList<>();
                    result.add("-,X,-,-,-,-,-,-,-,-");
                    result.add("PK10_SINGLE_DOUBLE");
                    break;
                case "大小单双季军":
                    result = new ArrayList<>();
                    result.add("-,-,X,-,-,-,-,-,-,-");
                    result.add("PK10_SINGLE_DOUBLE");
                    break;
                case "和值冠亚和值":
                case "和值前二和值":
                    result = new ArrayList<>();
                    result.add("X,X,-,-,-,-,-,-,-,-");
                    result.add("SUM");
                    break;
                case "和值中二和值":
                    result = new ArrayList<>();
                    result.add("-,-,-,-,X,X,-,-,-,-");
                    result.add("SUM");
                    break;
                case "和值后二和值":
                    result = new ArrayList<>();
                    result.add("-,-,-,-,-,-,-,-,X,X");
                    result.add("SUM");
                    break;
                case "和值前三和值":
                    result = new ArrayList<>();
                    result.add("X,X,X,-,-,-,-,-,-,-");
                    result.add("SUM");
                    break;
                case "和值后三和值":
                    result = new ArrayList<>();
                    result.add("-,-,-,-,-,-,-,X,X,X");
                    result.add("SUM");
                    break;
                case "龙虎冠军龙虎":
                    result = new ArrayList<>();
                    result.add("X,-,-,-,-,-,-,-,-,X");
                    result.add("DRAGON_TIGER_PK");
                    break;
                case "龙虎亚军龙虎":
                    result = new ArrayList<>();
                    result.add("-,X,-,-,-,-,-,-,X,-\"");
                    result.add("DRAGON_TIGER_PK");
                    break;
                case "龙虎季军龙虎":
                    result = new ArrayList<>();
                    result.add("-,-,X,-,-,-,-,X,-,-");
                    result.add("DRAGON_TIGER_PK");
                    break;
                case "龙虎第四名龙虎":
                    result = new ArrayList<>();
                    result.add("-,-,-,X,-,-,X,-,-,-");
                    result.add("DRAGON_TIGER_PK");
                    break;
                case "龙虎第五名龙虎":
                    result = new ArrayList<>();
                    result.add("-,-,-,-,X,X,-,-,-,-");
                    result.add("DRAGON_TIGER_PK");
                    break;
                case "冠军冠军":
                case "独赢独赢":
                    result = new ArrayList<>();
                    result.add("X,-,-,-,-,-,-,-,-,-");
                    result.add("NIUNIU");
                    break;
                case "连赢复式":
                case "连赢单式":
                case "前二复式":
                case "前二单式":
                    result = new ArrayList<>();
                    result.add("X,X,-,-,-,-,-,-,-,-");
                    result.add("NIUNIU");
                    break;
                case "三连环复式":
                case "三连环单式":
                case "前三复式":
                case "前三单式":
                    result = new ArrayList<>();
                    result.add("X,X,X,-,-,-,-,-,-,-");
                    result.add("");
                    break;
                case "四连环复式":
                case "四连环单式":
                case "前四复式":
                case "前四单式":
                    result = new ArrayList<>();
                    result.add("X,X,X,X,-,-,-,-,-,-");
                    result.add("");
                    break;
                case "五连环复式":
                case "五连环单式":
                case "前五复式":
                case "前五单式":
                    result = new ArrayList<>();
                    result.add("X,X,X,X,X,-,-,-,-,-");
                    result.add("");
                    break;
                case "六连环复式":
                case "六连环单式":
                case "前六复式":
                case "前六单式":
                    result = new ArrayList<>();
                    result.add("X,X,X,X,X,X,-,-,-,-");
                    result.add("");
                    break;
            }
        }

        if (Matchers._11x5Alias.contains(alias)) {
            switch (methodName) {
                case "三码前三直选复式":
                case "三码前三直选单式":
                case "三码前三组选复式":
                case "三码前三组选单式":
                    result = new ArrayList<>();
                    result.add("X,X,X,-,-");
                    result.add("");
                    break;
                case "二码前二直选复式":
                case "二码前二直选单式":
                case "二码前二组选复式":
                case "二码前二组选单式":
                    result = new ArrayList<>();
                    result.add("X,X,-,-,-");
                    result.add("");
                    break;
            }
        }

        if (Matchers.vnmNorAlias.contains(alias)) {
            result = new ArrayList<>();
            result.add("");
            result.add("VNM_NORTH");
        }

        if (Matchers.vnmNorAlias.contains(alias)) {
            result = new ArrayList<>();
            result.add("");
            result.add("VNM_NORTH");
        }

        if (Matchers.vnmMidSouAlias.contains(alias) || Matchers.vnmFastAlias.contains(alias)) {
            result = new ArrayList<>();
            result.add("");
            result.add("VNM_MIDDLE_SOUTH_FAST");
        }

        return result;
    }
}
