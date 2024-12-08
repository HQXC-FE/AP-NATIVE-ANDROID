package com.xtree.lottery.rule;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntryRule {
    Rules rules;
    Facts facts;
    RulesEngine rulesEngine;
    private static EntryRule INSTANCE;

    public static EntryRule getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EntryRule();
        }
        return INSTANCE;
    }

    public EntryRule() {
        facts = new Facts();
        rules = new Rules();
        rulesEngine = new DefaultRulesEngine();

        MatchRules.addRules(rules);
        FilterRules.addRules(rules);
        PrepareRules.addRules(rules);
        AttachedRules.addRules(rules);
        DecisionRules.addRules(rules);
        AfterRules.addRules(rules);
        EndingRules.addRules(rules);
    }

    public void startEngine(Facts facts) {
        // 范例
        Facts example = new Facts();
        String lotteryType = "ssc";
        String matcherName = "";
        String betTimes = "1";
        String betDisplayMoney = "2";
        String betPrize = "2";
        String currentCategoryName = "后三码";
        String currentCategoryCateName = "后三码";
        String currentCategorySelectareaType = "digital";
        String currentCategoryFlag = "hn5fc";
        String currentMethodGroupName = "后三直选";
        String currentMethodName = "复式";
        String currentMethodLotteryId = "14";
        String currentMethodCodeSP = "";
        String currentMethodCateTitle = "后三码";
        String currentMethodDesc = "组选和值";
        List<String> betCodes = new ArrayList<>();
        betCodes.add("5");
        betCodes.add("6");
        betCodes.add("7");
        String betPoschoose = "";

        HashMap<String, Object> currentMethod = new HashMap<>();
        HashMap<String, String> currentCategory = new HashMap<>();
        List<Map<String, String>> currentMethodModes = new ArrayList<>();
        Map<String, String> currentMethodModesitem1 = new HashMap<>();
        Map<String, String> currentMethodModesitem2 = new HashMap<>();
        Map<String, String> currentMethodModesitem3 = new HashMap<>();
        Map<String, String> currentMethodModesitem4 = new HashMap<>();
        List<Map<String, String>> currentMethodPrizeLevel = new ArrayList<>();
        Map<String, String> currentMethodPrizeLevelItem1 = new HashMap<>();
        HashMap<String, Object> currentMethodSelectarea = new HashMap<>();
        List<Map<String, String>> currentMethodSelectareaLayout = new ArrayList<>();
        HashMap<String, String> currentMethodSelectareaLayouTitem1 = new HashMap<>();
        HashMap<String, String> currentMethodSelectareaLayouTitem2 = new HashMap<>();
        HashMap<String, String> currentMethodSelectareaLayouTitem3 = new HashMap<>();
        HashMap<String, Object> bet = new HashMap<>();
        Map<String, String> betModeitem = new HashMap<>();
        Map<String, String> betDisplay = new HashMap<>();

        matcherName = currentCategoryCateName + "-" + currentMethodGroupName + "-" + currentMethodName;

        currentMethod.put("groupName", currentMethodGroupName);
        currentMethod.put("lotteryId", currentMethodLotteryId);
        currentMethodSelectareaLayouTitem1.put("title", "百位");
        currentMethodSelectareaLayouTitem1.put("no", "0|1|2|3|4|5|6|7|8|9");
        currentMethodSelectareaLayouTitem1.put("place", "0");
        currentMethodSelectareaLayouTitem1.put("cols", "1");
        currentMethodSelectareaLayouTitem2.put("title", "十位");
        currentMethodSelectareaLayouTitem2.put("no", "0|1|2|3|4|5|6|7|8|9");
        currentMethodSelectareaLayouTitem2.put("place", "1");
        currentMethodSelectareaLayouTitem2.put("cols", "1");
        currentMethodSelectareaLayouTitem3.put("title", "个位");
        currentMethodSelectareaLayouTitem3.put("no", "0|1|2|3|4|5|6|7|8|9");
        currentMethodSelectareaLayouTitem3.put("place", "2");
        currentMethodSelectareaLayouTitem3.put("cols", "1");
        currentMethodSelectareaLayout.add(currentMethodSelectareaLayouTitem1);
        currentMethodSelectareaLayout.add(currentMethodSelectareaLayouTitem2);
        currentMethodSelectareaLayout.add(currentMethodSelectareaLayouTitem3);
        currentMethodSelectarea.put("layout", currentMethodSelectareaLayout);
        currentMethodSelectarea.put("type", currentCategorySelectareaType);
        currentMethod.put("selectarea", currentMethodSelectarea);
        currentMethodModesitem1.put("modeid", "1");
        currentMethodModesitem1.put("name", "元");
        currentMethodModesitem1.put("rate", "1");
        currentMethodModesitem2.put("modeid", "2");
        currentMethodModesitem2.put("name", "角");
        currentMethodModesitem2.put("rate", "0.1");
        currentMethodModesitem3.put("modeid", "3");
        currentMethodModesitem3.put("name", "分");
        currentMethodModesitem3.put("rate", "0.01");
        currentMethodModesitem4.put("modeid", "4");
        currentMethodModesitem4.put("name", "厘");
        currentMethodModesitem4.put("rate", "0.001");
        currentMethodModes.add(currentMethodModesitem1);
        currentMethodModes.add(currentMethodModesitem2);
        currentMethodModes.add(currentMethodModesitem3);
        currentMethodModes.add(currentMethodModesitem4);
        currentMethod.put("modes", currentMethodModes);
        currentMethod.put("code_sp", currentMethodCodeSP);
        currentMethodPrizeLevelItem1.put("1", "1900.00");
        currentMethodPrizeLevel.add(currentMethodPrizeLevelItem1);
        currentMethod.put("prize_level", currentMethodPrizeLevel);
        currentMethod.put("cate_title", currentMethodCateTitle);
        currentMethod.put("desc", currentMethodDesc);
        currentCategory.put("name", currentCategoryName);
        currentCategory.put("flag", currentCategoryFlag);
        bet.put("codes", betCodes);
        bet.put("poschoose", betPoschoose);
        betModeitem.put("modeid", "1");
        betModeitem.put("name", "元");
        betModeitem.put("rate", "1");
        bet.put("times", betTimes);
        bet.put("prize", betPrize);
        betDisplay.put("money", betDisplayMoney);

        example.put("lotteryType", lotteryType);
        example.put("matcherName", matcherName);
        example.put("currentMethod", currentMethod);
        example.put("currentCategory", currentCategory);
        example.put("bet", bet);

        rulesEngine.fire(rules, example);
    }
}
