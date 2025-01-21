package com.xtree.lottery.rule.betting.match;

import com.xtree.base.utils.CfLog;
import com.xtree.lottery.rule.betting.Matchers;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.List;

@Rule(name = "Match Rule Token to Rule Set", description = "Matches the rule token to a rule set")
public class MatchRuleTokenToSetRule {

    @Priority
    public int getPriority() {
        return -99800;
    }

    @Condition
    public boolean when(Facts facts) {
        String ruleSuite = facts.get("ruleSuite");
        String lotteryType = facts.get("lotteryType");
        return ruleSuite == null && "ssc".equals(lotteryType);
    }

    @Action
    public void then(Facts facts) {
        try {
            String token = facts.get("token");
            List<String> ruleSuite = Matchers.token2Rules(token);
            facts.put("ruleSuite", ruleSuite);
        } catch (Exception e) {
            CfLog.e(e.getMessage());
        }
    }
}