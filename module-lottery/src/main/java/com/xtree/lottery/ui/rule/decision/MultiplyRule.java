package com.xtree.lottery.ui.rule.decision;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;

import java.util.List;
import java.util.Map;

@Rule(name = "MultiplyRule", description = "单纯的倍数相乘")
public class MultiplyRule {

    @Priority
    public int getPriority() {
        return 17700;
    }

    @Condition
    public boolean when(Map<String, Object> facts) {
        List<String> ruleSuite = (List<String>) facts.get("ruleSuite");
        return ruleSuite.stream().anyMatch(item -> item.matches("^multiply-.*$"));
    }

    @Action
    public void then(Map<String, Object> facts) {
        List<String> ruleSuite = (List<String>) facts.get("ruleSuite");

        String matchedRule = ruleSuite.stream().filter(item -> item.matches("^multiply-.*$")).findFirst().orElse("");
        int multiplier = Integer.parseInt(matchedRule.split("multiply-")[1]);

        int num = (int) facts.get("num");
        num *= multiplier;

        facts.put("num", num);
    }
}
