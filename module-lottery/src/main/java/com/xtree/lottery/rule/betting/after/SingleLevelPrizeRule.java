package com.xtree.lottery.rule.betting.after;

import com.xtree.base.utils.CfLog;
import com.xtree.lottery.rule.betting.Matchers;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.List;
import java.util.Map;

@Rule(name = "SingleLevelPrizeRule", description = "一号多注 && 单级奖级规则")
public class SingleLevelPrizeRule {

    @Priority
    public int getPriority() {
        return -8790;
    }

    @Condition
    public boolean when(Facts facts) {
        List<String> applicableCategories = List.of("包组", "头", "头与尾");

        Map<String, String> currentCategory = facts.get("currentCategory");
        String currentCategoryName = currentCategory.get("name");
        String currentCategoryFlag = currentCategory.get("flag");

        return facts.get("currentPrizeModes") != null &&
                applicableCategories.contains(currentCategoryName) &&
                (Matchers.vnmNorAlias.contains(currentCategoryFlag) ||
                        Matchers.vnmMidSouAlias.contains(currentCategoryFlag) ||
                        Matchers.vnmFastAlias.contains(currentCategoryFlag));
    }

    @Action
    public void then(Facts facts) {
        try {
            double currentPrize = facts.get("currentPrize");
            double money = facts.get("money");
            Map<String, Integer> attached = facts.get("attached");
            int attachedNumber = attached.get("number");

            if (attachedNumber == 1) {
                facts.put("currentBonus", currentPrize - money);
            } else {
                double bonusRange = currentPrize * attachedNumber - money;
                facts.put("currentBonus", currentPrize + "~" + Math.round(bonusRange * 10000.0) / 10000.0);
                facts.put("currentPrize", currentPrize + "~" + Math.round(currentPrize * attachedNumber * 10000.0) / 10000.0);
            }
        } catch (Exception e) {
            CfLog.e(e.getMessage());
        }
    }
}
