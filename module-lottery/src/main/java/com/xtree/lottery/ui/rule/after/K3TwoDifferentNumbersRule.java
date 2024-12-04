package com.xtree.lottery.ui.rule.after;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;

import java.util.List;
import java.util.Map;

@Rule(name = "K3TwoDifferentNumbersRule", description = "快三 - 二不同号")
public class K3TwoDifferentNumbersRule {

    @Priority
    public int getPriority() {
        return 8791;
    }

    @Condition
    public boolean when(Map<String, Object> facts) {
        List<String> k3Alias = (List<String>) facts.get("k3Alias");
        String currentCategoryName = (String) facts.get("currentCategoryName");
        String currentCategoryFlag = (String) facts.get("currentCategoryFlag");

        return facts.get("currentPrizeModes") != null &&
                "二不同号".equals(currentCategoryName) &&
                k3Alias.contains(currentCategoryFlag);
    }

    @Action
    public void then(Map<String, Object> facts) {
        String methodName = (String) facts.get("currentCategoryName") + facts.get("currentMethodName");
        double currentPrize = (double) facts.get("currentPrize");
        double money = (double) facts.get("money");
        List<List<String>> formatCodes = (List<List<String>>) facts.get("formatCodes");

        if ("二不同号标准选号".equals(methodName)) {
            if (formatCodes.get(0).size() >= 3) {
                double bonusRange = currentPrize * 3 - money;
                facts.put("currentBonus", currentPrize + "~" + Math.round(bonusRange * 10000.0) / 10000.0);
                facts.put("currentPrize", currentPrize + "~" + Math.round(currentPrize * 3 * 10000.0) / 10000.0);
            }
        }
    }
}
