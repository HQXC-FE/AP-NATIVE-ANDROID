package com.xtree.lottery.rule.decision;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.List;
import java.util.Map;

@Rule(name = "MultiplyRule", description = "单纯的倍数相乘")
public class MultiplyRule {

    @Priority
    public int getPriority() {
        return 17700;
    }

    @Condition
    public boolean when(Facts facts) {
        List<String> ruleSuite = facts.get("ruleSuite");
        return ruleSuite.stream().anyMatch(item -> item.matches("^multiply-.*$"));
    }

    @Action
    public void then(Facts facts) {
        List<String> ruleSuite = facts.get("ruleSuite");

        String matchedRule = ruleSuite.stream().filter(item -> item.matches("^multiply-.*$")).findFirst().orElse("");
        int multiplier = Integer.parseInt(matchedRule.split("multiply-")[1]);

        int num = facts.get("num");
        num *= multiplier;

        facts.put("num", num);
    }
}
