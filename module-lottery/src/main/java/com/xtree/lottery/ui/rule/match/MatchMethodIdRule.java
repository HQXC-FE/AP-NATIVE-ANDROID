package com.xtree.lottery.ui.rule.match;

import com.xtree.lottery.ui.rule.Matchers;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;


@Rule(name = "Match MethodId to Rule Token", description = "Matches the MethodId to a rule token")
public class MatchMethodIdRule {

    @Priority
    public int getPriority() {
        return 100000;
    }

    @Condition
    public boolean when(Facts facts) {
        String token = facts.get("token");
        String lotteryType = facts.get("lotteryType");
        return token == null && "ssc".equals(lotteryType);
    }

    @Action
    public void then(Facts facts) {
        String matcherName = facts.get("matcherName");
        String token = Matchers.method2Token(matcherName);
        facts.put("token", token);
    }
}