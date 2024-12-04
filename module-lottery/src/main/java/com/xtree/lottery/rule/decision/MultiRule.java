package com.xtree.lottery.rule.decision;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;

import java.util.List;
import java.util.Map;

@Rule(name = "MultiRule", description = "复式玩法规则")
public class MultiRule {

    @Priority
    public int getPriority() {
        return 20000;
    }

    @Condition
    public boolean when(Map<String, Object> facts) {
        List<String> ruleSuite = (List<String>) facts.get("ruleSuite");
        return ruleSuite.contains("multi");
    }

    @Action
    public void then(Map<String, Object> facts) {
        List<List<String>> formatCodes = (List<List<String>>) facts.get("formatCodes");
        int num = formatCodes.stream()
                .mapToInt(List::size)
                .reduce(1, (a, b) -> a * b); // 乘积计算

        facts.put("num", num);
    }
}
