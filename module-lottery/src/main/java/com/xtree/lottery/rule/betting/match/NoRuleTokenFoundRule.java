package com.xtree.lottery.rule.betting.match;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.List;

@Rule(name = "No Rule Token Found", description = "Handles the case where no rule token is found")
public class NoRuleTokenFoundRule {

    @Priority
    public int getPriority() {
        return -99900;
    }

    @Condition
    public boolean when(Facts facts) {
        String token = facts.get("token");
        String lotteryType = facts.get("lotteryType");
        return token == null && "ssc".equals(lotteryType);
    }

    @Action
    public void then(Facts facts) {
        try {
            List<String> message = facts.get("message");
            message.add("没有找到玩法对应的规则指纹");
        } catch (Exception e) {
            CfLog.e(e.getMessage());
        }
    }
}