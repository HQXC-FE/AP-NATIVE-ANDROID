package com.xtree.lottery.ui.rule.decision;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;

import java.util.List;
import java.util.Map;

@Rule(name = "GeneralElectionRule", description = "通选算法")
public class GeneralElectionRule {

    @Priority
    public int getPriority() {
        return 19400;
    }

    @Condition
    public boolean when(Map<String, Object> facts) {
        List<String> ruleSuite = (List<String>) facts.get("ruleSuite");
        return ruleSuite.contains("general-election");
    }

    @Action
    public void then(Map<String, Object> facts) {
        List<List<String>> formatCodes = (List<List<String>>) facts.get("formatCodes");

        if (formatCodes != null && !formatCodes.isEmpty() && "通选".equals(formatCodes.get(0).get(0))) {
            facts.put("num", 1);
        } else {
            facts.put("num", 0);
        }
    }
}
