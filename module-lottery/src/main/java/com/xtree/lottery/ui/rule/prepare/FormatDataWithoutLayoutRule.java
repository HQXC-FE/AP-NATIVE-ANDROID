package com.xtree.lottery.ui.rule.prepare;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;

import java.util.Map;

@Rule(name = "Format Data Without Layout", description = "格式化数据格式（无布局）")
public class FormatDataWithoutLayoutRule {

    @Priority
    public int getPriority() {
        return 89900;
    }

    @Condition
    public boolean when(Map<String, Object> facts) {
        return "ssc".equals(facts.get("lotteryType")) && !((Map<?, ?>) facts.get("currentMethod")).containsKey("selectarea.layout");
    }

    @Action
    public void then(Map<String, Object> facts) {
        String betCodes = (String) ((Map<?, ?>) facts.get("bet")).get("codes");
        String formatCodes = betCodes.trim();
        facts.put("formatCodes", formatCodes);
    }
}
