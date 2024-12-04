package com.xtree.lottery.rule.decision;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;

import java.util.List;
import java.util.Map;

@Rule(name = "CombinationRule", description = "组合玩法规则")
public class CombinationRule {

    @Priority
    public int getPriority() {
        return 19800;
    }

    @Condition
    public boolean when(Map<String, Object> facts) {
        List<String> ruleSuite = (List<String>) facts.get("ruleSuite");
        return ruleSuite.contains("combination");
    }

    @Action
    public void then(Map<String, Object> facts) {
        List<List<String>> formatCodes = (List<List<String>>) facts.get("formatCodes");
        int num = formatCodes.size();

        for (List<String> item : formatCodes) {
            num *= item.size();
        }

        facts.put("num", num);
    }
}

