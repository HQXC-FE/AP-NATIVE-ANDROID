package com.xtree.lottery.rule.betting.match;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.List;

@Rule(name = "No Rule Set Found", description = "Handles the case where no rule set is found")
public class NoRuleSetFoundRule {

    @Priority
    public int getPriority() {
        return -99700;
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
            List<String> message = facts.get("message");
            message.add("没有找到规则指纹对应的规则集合");
            facts.put("message", message);
        } catch (Exception e) {
            CfLog.e(e.getMessage());
        }
    }
}
