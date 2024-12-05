package com.xtree.lottery.rule.decision;

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
        return 17900;
    }

    @Condition
    public boolean when(Facts facts) {
        List<String> ruleSuite = facts.get("ruleSuite");
        return ruleSuite != null && ruleSuite.stream().anyMatch(item -> item.matches("^add-bet-num-.*$"));
    }

    @Action
    public void then(Facts facts) {
        List<String> ruleSuite = facts.get("ruleSuite");
        String currentCategoryFlag = facts.get("currentCategory.flag");
        String currentCategoryName = facts.get("currentCategory.name");
        String currentMethodGroupName = facts.get("currentMethod.groupName");
        List<String> vnmMidSouAlias = facts.get("vnmMidSouAlias");
        List<String> vnmFastAlias = facts.get("vnmFastAlias");
        List<Map<String, Object>> mapMatchNameToNumList = facts.get("mapMatchNameToNumList");
        int num = facts.get("num");

        // 判断是否属于特定彩种
        if (vnmMidSouAlias.contains(currentCategoryFlag) || vnmFastAlias.contains(currentCategoryFlag)) {
            String matchName = (currentCategoryName + "-" + currentMethodGroupName).trim();
            for (Map<String, Object> item : mapMatchNameToNumList) {
                if (matchName.equals(item.get("name"))) {
                    num *= (int) item.get("num");
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
    }
}
