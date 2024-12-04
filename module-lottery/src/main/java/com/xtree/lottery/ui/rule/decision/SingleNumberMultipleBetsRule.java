package com.xtree.lottery.ui.rule.decision;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;

import java.util.List;
import java.util.Map;

@Rule(name = "SingleNumberMultipleBetsRule", description = "单式 一号多注")
public class SingleNumberMultipleBetsRule {

    @Priority
    public int getPriority() {
        return 17900;
    }

    @Condition
    public boolean when(Map<String, Object> facts) {
        List<String> ruleSuite = (List<String>) facts.get("ruleSuite");
        return ruleSuite.stream().anyMatch(item -> item.matches("^add-bet-num-.*$"));
    }

    @Action
    public void then(Map<String, Object> facts) {
        List<String> ruleSuite = (List<String>) facts.get("ruleSuite");
        Map<String, Object> currentCategory = (Map<String, Object>) facts.get("currentCategory");
        String currentFlag = (String) currentCategory.get("flag");
        List<String> vnmMidSouAlias = (List<String>) facts.get("vnmMidSouAlias");
        List<String> vnmFastAlias = (List<String>) facts.get("vnmFastAlias");

        int num = (int) facts.get("num");

        if (vnmMidSouAlias.contains(currentFlag) || vnmFastAlias.contains(currentFlag)) {
            List<Map<String, Object>> mapMatchNameToNumList = (List<Map<String, Object>>) facts.get("mapMatchNameToNumList");
            String matchName = currentCategory.get("name") + "-" + facts.get("currentMethod.groupName");
            for (Map<String, Object> item : mapMatchNameToNumList) {
                if (matchName.trim().equals(item.get("name"))) {
                    num *= (int) item.get("num");
                }
            }
        } else {
            String matchedRule = ruleSuite.stream().filter(item -> item.matches("^add-bet-num-.*$")).findFirst().orElse("");
            int multiplier = Integer.parseInt(matchedRule.split("add-bet-num-")[1]);
            num *= multiplier;
        }

        facts.put("num", num);
    }
}
