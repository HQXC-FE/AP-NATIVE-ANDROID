package com.xtree.lottery.ui.rule.prepare;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;

import java.util.List;
import java.util.Map;

@Rule(name = "Preserve Original Format", description = "保持原有格式")
public class PreserveOriginalFormatRule {

    @Priority
    public int getPriority() {
        return 80001;
    }

    @Condition
    public boolean when(Map<String, Object> facts) {
        List<?> ruleSuite = (List<?>) facts.get("ruleSuite");
        return "ssc".equals(facts.get("lotteryType")) && ruleSuite.contains("without-format-codes");
    }

    @Action
    public void then(Map<String, Object> facts) {
        String betCodes = (String) ((Map<?, ?>) facts.get("bet")).get("codes");
        facts.put("formatCodes", betCodes);
    }
}