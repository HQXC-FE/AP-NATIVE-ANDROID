package com.xtree.lottery.ui.rule.after;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;

import java.util.List;
import java.util.Map;

@Rule(name = "SingleLevelPrizeRule", description = "一号多注 && 单级奖级规则")
public class SingleLevelPrizeRule {

    @Priority
    public int getPriority() {
        return 8790;
    }

    @Condition
    public boolean when(Map<String, Object> facts) {
        List<String> applicableCategories = List.of("包组", "头", "头与尾");
        List<String> categoryFlags = (List<String>) facts.get("vnmNorAlias");
        categoryFlags.addAll((List<String>) facts.get("vnmMidSouAlias"));
        categoryFlags.addAll((List<String>) facts.get("vnmFastAlias"));

        String currentCategoryName = (String) facts.get("currentCategoryName");
        String currentCategoryFlag = (String) facts.get("currentCategoryFlag");

        return facts.get("currentPrizeModes") != null &&
                applicableCategories.contains(currentCategoryName) &&
                categoryFlags.contains(currentCategoryFlag);
    }

    @Action
    public void then(Map<String, Object> facts) {
        double currentPrize = (double) facts.get("currentPrize");
        double money = (double) facts.get("money");
        int attachedNumber = (int) facts.get("attachedNumber");

        if (attachedNumber == 1) {
            facts.put("currentBonus", currentPrize - money);
        } else {
            double bonusRange = currentPrize * attachedNumber - money;
            facts.put("currentBonus", currentPrize + "~" + Math.round(bonusRange * 10000.0) / 10000.0);
            facts.put("currentPrize", currentPrize + "~" + Math.round(currentPrize * attachedNumber * 10000.0) / 10000.0);
        }
    }
}
