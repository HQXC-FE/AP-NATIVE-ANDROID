package com.xtree.lottery.rule.betting.after;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.List;
import java.util.Map;

@Rule(name = "Lottery Prize Rule", description = "Set prize based on lottery type and conditions")
public class LotteryPrizeRule {

    @Priority
    public int getPriority() {
        return -8846;
    }

    @Condition
    public boolean when(Facts facts) {
        String lotteryType = facts.get("lotteryType");
        Boolean prize = facts.get("prize");
        Object currentMethod = facts.get("currentMethod");

        // Assuming `currentMethod.prize_group` is passed as a list in facts
        List<String> prizeGroup = (currentMethod instanceof Map)
                ? (List<String>) ((Map<?, ?>) currentMethod).get("prize_group")
                : null;

        return "ssc".equals(lotteryType) && prize == null && prizeGroup != null && !prizeGroup.isEmpty();
    }

    @Action
    public void then(Facts facts) {
        try {
            // Assuming `bet` is an object stored in facts with a "prize" property
            Map<String, Object> bet = facts.get("bet");
            Object prize = bet != null ? bet.get("prize") : null;

            facts.put("prize", prize);
        } catch (Exception e) {
            CfLog.e(e.getMessage());
        }
    }
}

