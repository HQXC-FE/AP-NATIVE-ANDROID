package com.xtree.lottery.rule.after;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.Map;

@Rule(name = "Set Times", description = "Set default times if undefined")
public class SetTimesRule {

    @Priority
    public int getPriority() {
        return 8848;
    }

    @Condition
    public boolean when(Facts facts) {
        String lotteryType = facts.get("lotteryType");
        Integer times = facts.get("times");
        return "ssc".equals(lotteryType) && times == null;
    }

    @Action
    public void then(Facts facts) {
        Map<String, Object> bet = facts.get("bet");
        Integer times = (Integer) bet.get("times");
        facts.put("times", times != null ? times : 1);
    }
}
