package com.xtree.lottery.rule.betting.decision;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.List;

@Rule(name = "GeneralElectionRule", description = "通选算法")
public class GeneralElectionRule {

    @Priority
    public int getPriority() {
        return -19400;
    }

    @Condition
    public boolean when(Facts facts) {
        List<String> ruleSuite = facts.get("ruleSuite");
        return ruleSuite.contains("general-election");
    }

    @Action
    public void then(Facts facts) {
        try {
            List<List<String>> formatCodes = facts.get("formatCodes");

            if (formatCodes != null && !formatCodes.isEmpty() && "通选".equals(formatCodes.get(0).get(0))) {
                facts.put("num", 1);
            } else {
                facts.put("num", 0);
            }
        } catch (Exception e) {
            CfLog.e(e.getMessage());
        }
    }
}
