package com.xtree.lottery.rule.betting.match;

import com.xtree.base.utils.CfLog;
import com.xtree.lottery.rule.betting.Matchers;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.List;
import java.util.Map;

@Rule(name = "LHC Rule Set", description = "Handles LHC specific rule set matching")
public class LHCRuleSetRule {

    @Priority
    public int getPriority() {
        return -99600;
    }

    @Condition
    public boolean when(Facts facts) {
        String lotteryType = facts.get("lotteryType");
        return "lhc".equals(lotteryType);
    }

    @Action
    public void then(Facts facts) {
        try {
            String lotteryId = ((Map<String, String>) facts.get("currentMethod")).get("lotteryId");
            List<String> ruleSuite = Matchers.mathLhcRules(Integer.valueOf(lotteryId));
            facts.put("ruleSuite", ruleSuite);
        } catch (Exception e) {
            CfLog.e(e.getMessage());
        }
    }
}