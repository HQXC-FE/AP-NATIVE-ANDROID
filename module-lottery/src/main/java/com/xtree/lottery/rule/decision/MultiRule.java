package com.xtree.lottery.rule.decision;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.List;
import java.util.Map;

@Rule(name = "MultiRule", description = "复式玩法规则")
public class MultiRule {

    @Priority
    public int getPriority() {
        return 20000;
    }

    @Condition
    public boolean when(Facts facts) {
        List<String> ruleSuite = facts.get("ruleSuite");
        return ruleSuite.contains("multi");
    }

    @Action
    public void then(Facts facts) {
        List<List<String>> formatCodes = facts.get("formatCodes");
        int num = formatCodes.stream()
                .mapToInt(List::size)
                .reduce(1, (a, b) -> a * b);  // 计算所有子列表长度的乘积

        facts.put("num", num);
    }
}
