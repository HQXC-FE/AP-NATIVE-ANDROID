package com.xtree.lottery.rule.prepare;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.List;
import java.util.Map;

@Rule(name = "Keep Original Format", description = "Keeps the original format if ruleSuite includes 'without-format-codes'")
public class PreserveOriginalFormatRule {

    @Priority
    public int getPriority() {
        return -80001;
    }

    @Condition
    public boolean when(Facts facts) {
        String lotteryType = facts.get("lotteryType");
        List<String> ruleSuite = facts.get("ruleSuite");
        return "ssc".equals(lotteryType) && ruleSuite.contains("without-format-codes");
    }

    @Action
    public void then(Facts facts) {
        Map<String, List<String>> bet = facts.get("bet");
        List<String> betCodes = bet.get("codes");
        facts.put("formatCodes", betCodes);  // 直接保持原有格式
    }
}
