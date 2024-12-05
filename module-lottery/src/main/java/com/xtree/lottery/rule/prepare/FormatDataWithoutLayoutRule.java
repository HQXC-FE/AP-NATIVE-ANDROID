package com.xtree.lottery.rule.prepare;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.Map;

@Rule(name = "Format Data Without Layout", description = "Formats the codes when there is no layout and lottery type is 'ssc'")
public class FormatDataWithoutLayoutRule {

    @Priority
    public int getPriority() {
        return 89900;
    }

    @Condition
    public boolean when(Facts facts) {
        String lotteryType = facts.get("lotteryType");
        Map<String, Object> currentMethod = facts.get("currentMethod");
        return "ssc".equals(lotteryType) && (!currentMethod.containsKey("selectarea")
                || !((Map<?, ?>) currentMethod.get("selectarea")).containsKey("layout"));
    }

    @Action
    public void then(Facts facts) {
        String betCodes = facts.get("betCodes");
        String formatCodes = betCodes.trim();  // 去除前后空格
        facts.put("formatCodes", formatCodes);
    }
}
