package com.xtree.lottery.rule.betting.decision;

import com.xtree.base.utils.CfLog;
import com.xtree.lottery.rule.betting.Matchers;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.List;
import java.util.Map;

@Rule(name = "SingleNumberMultipleBetsRule", description = "单式 一号多注")
public class SingleNumberMultipleBetsRule {

    @Priority
    public int getPriority() {
        return -17900;
    }

    @Condition
    public boolean when(Facts facts) {
        List<String> ruleSuite = facts.get("ruleSuite");
        return ruleSuite != null && ruleSuite.stream().anyMatch(item -> item.matches("^add-bet-num-.*$"));
    }

    @Action
    public void then(Facts facts) {
        try {
            List<String> ruleSuite = facts.get("ruleSuite");
            Map<String, String> currentCategory = facts.get("currentCategory");
            Map<String, String> currentMethod = facts.get("currentMethod");
            String currentCategoryFlag = currentCategory.get("flag");
            String currentCategoryName = currentCategory.get("name");
            String currentMethodGroupName = currentMethod.get("groupName");
            int num = facts.get("num");

            // 判断是否属于特定彩种
            if (Matchers.vnmMidSouAlias.contains(currentCategoryFlag) || Matchers.vnmFastAlias.contains(currentCategoryFlag)) {
                String matchName = (currentCategoryName + "-" + currentMethodGroupName).trim();
                for (Map.Entry<String, Integer> item : Matchers.mapMatchNameToNumList.entrySet()) {
                    if (matchName.equals(item.getKey())) {
                        num *= item.getValue();
                        break;
                    }
                }
            } else {
                // 处理一般情况
                String matchedRule = ruleSuite.stream()
                        .filter(item -> item.matches("^add-bet-num-.*$"))
                        .findFirst()
                        .orElse(null);

                if (matchedRule != null) {
                    int multiplier = Integer.parseInt(matchedRule.split("add-bet-num-")[1]);
                    num *= multiplier;
                }
            }

            facts.put("num", num);
        } catch (Exception e) {
            CfLog.e(e.getMessage());
        }
    }
}
