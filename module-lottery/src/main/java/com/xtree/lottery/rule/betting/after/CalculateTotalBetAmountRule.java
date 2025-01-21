package com.xtree.lottery.rule.betting.after;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.Map;

@Rule(name = "Calculate Total Bet Amount", description = "Calculate the total bet amount based on parameters")
public class CalculateTotalBetAmountRule {

    @Priority
    public int getPriority() {
        return -8847;
    }

    @Condition
    public boolean when(Facts facts) {
        String lotteryType = facts.get("lotteryType");
        Double money = facts.get("money");
        return "ssc".equals(lotteryType) && money == null;
    }

    @Action
    public void then(Facts facts) {
        try {
            Integer num = facts.get("num");
            Integer times = facts.get("times");
            Map<String, Object> mode = facts.get("mode");

            if (num != null && times != null && mode != null) {
                Double rate = Double.parseDouble((String) mode.get("rate"));
                if (rate != null) {
                    Double money = Math.round(num * times * rate * 2 * 1000.0) / 1000.0;
                    facts.put("money", money);
                }
            }
        } catch (Exception e) {
            CfLog.e(e.getMessage());
        }
    }
}