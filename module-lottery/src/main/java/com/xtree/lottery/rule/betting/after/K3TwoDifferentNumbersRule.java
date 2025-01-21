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

@Rule(name = "K3TwoDifferentNumbersRule", description = "快三 - 二不同号")
public class K3TwoDifferentNumbersRule {

    @Priority
    public int getPriority() {
        return -8791;
    }

    @Condition
    public boolean when(Facts facts) {
        Map<String, String> currentCategory = facts.get("currentCategory");
        String currentCategoryName = currentCategory.get("name");
        String currentCategoryFlag = currentCategory.get("flag");

        return facts.get("currentPrizeModes") != null &&
                "二不同号".equals(currentCategoryName) &&
                Matchers.k3Alias.contains(currentCategoryFlag);
    }

    @Action
    public void then(Facts facts) {
        try {
            Map<String, String> currentCategory = facts.get("currentCategory");
            Map<String, String> currentMethod = facts.get("currentMethod");
            String methodName = currentCategory.get("name") + currentMethod.get("name");
            double currentPrize = facts.get("currentPrize");
            double money = facts.get("money");
            List<List<String>> formatCodes = facts.get("formatCodes");

            if ("二不同号标准选号".equals(methodName)) {
                if (formatCodes.get(0).size() >= 3) {
                    double bonusRange = currentPrize * 3 - money;
                    facts.put("currentBonus", currentPrize + "~" + Math.round(bonusRange * 10000.0) / 10000.0);
                    facts.put("currentPrize", currentPrize + "~" + Math.round(currentPrize * 3 * 10000.0) / 10000.0);
                }
            }
        } catch (Exception e) {
            CfLog.e(e.getMessage());
        }
    }
}
