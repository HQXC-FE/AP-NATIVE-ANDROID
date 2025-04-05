package com.xtree.lottery.rule;

import com.xtree.lottery.rule.betting.GameRule;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SpecialSingle {
    public static final Map<String, GameRule> SPECIAL_SINGLE = new HashMap<>();

    static {
        SPECIAL_SINGLE.put("rank", new GameRule(
                Arrays.asList("猜前二-猜前二-单式", "猜前三-猜前三-单式", "猜前四-猜前四-单式", "猜前五-猜前五-单式", "猜前六-猜前六-单式", "连赢-连赢-单式", "三连环-三连环-单式", "四连环-四连环-单式", "五连环-五连环-单式", "六连环-六连环-单式", "前二-猜前二-单式", "前三-猜前三-单式", "前四-猜前四-单式", "前五-猜前五-单式", "前六-猜前六-单式"),
                "(0[1-9]\\s|10\\s){$}(0[1-9]|10)",
                "rankFilter",
                "[,\\s]+",
                true,
                "每注号码之间请使用逗号【，】、分号【；】 或回车隔开。注意：号码之间要用空格隔开，不足2位要在前面加0。"
        ));

        SPECIAL_SINGLE.put("choose5", new GameRule(
                Arrays.asList("三码-三码-前三直选单式", "三码-三码-前三组选单式", "二码-二码-前二直选单式", "二码-二码-前二组选单式", "任选单式-任选单式-一中一", "任选单式-任选单式-二中二", "任选单式-任选单式-三中三", "任选单式-任选单式-四中四", "任选单式-任选单式-五中五", "任选单式-任选单式-六中五", "任选单式-任选单式-七中五", "任选单式-任选单式-八中五"),
                "(0[1-9]\\s|10\\s|11\\s){$}(0[1-9]|10|11)",
                "choose5Filter",
                "[,\\s]+",
                true,
                "每注号码之间请使用逗号【，】、分号【；】 或回车隔开。注意：号码之间要用空格隔开，不足2位要在前面加0。"
        ));

        SPECIAL_SINGLE.put("k3", new GameRule(
                Arrays.asList("三不同号-三不同号-手动输入"),
                "(?!\\d*?(\\d)\\d*?\\1)[1-6\\s]{$}",
                "k3Filter",
                " ",
                false,
                "每注号码之间请用一个空格【 】、逗号【，】或者分号【；】隔开"
        ));

        SPECIAL_SINGLE.put("dif2", new GameRule(
                Arrays.asList("二不同号-二不同号-手动输入"),
                "(?!\\d*?(\\d)\\d*?\\1)[1-6\\s]{$}",
                "dif2Filter",
                " ",
                false,
                "每注号码之间请用一个空格【 】、逗号【，】或者分号【；】隔开"
        ));

        SPECIAL_SINGLE.put("sk2", new GameRule(
                Arrays.asList("二同号单选-二同号单选-手动输入"),
                "[1-6\\s]{$}",
                "sk2Filter",
                " ",
                false,
                "每注号码之间请用一个空格【 】、逗号【，】或者分号【；】隔开"
        ));

        SPECIAL_SINGLE.put("default", new GameRule(
                Collections.emptyList(),
                "[0-9\\s]{$}",
                "defaultFilter",
                " ",
                false,
                "每注号码之间请用一个空格【 】、逗号【，】或者分号【；】隔开"
        ));
    }

    public static Map<String, GameRule> getSingleMethod() {
        return SPECIAL_SINGLE;
    }
}