package com.xtree.lottery.rule.prepare;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Rule(name = "Format Box Type", description = "格式化BOX类型")
public class FormatBoxTypeRule {

    @Priority
    public int getPriority() {
        return 89800;
    }

    @Condition
    public boolean when(Map<String, Object> facts) {
        return "ssc".equals(facts.get("lotteryType")) && "box".equals(((Map<?, ?>) facts.get("currentMethod")).get("selectarea.type"));
    }

    @Action
    public void then(Map<String, Object> facts) {
        List<Object> formatCodes = new ArrayList<>();
        List<?> betCodes = (List<?>) ((Map<?, ?>) facts.get("bet")).get("codes");

        for (Object item : betCodes) {
            List<Object> codeList = new ArrayList<>();
            codeList.add(item);
            formatCodes.add(codeList);
        }

        facts.put("formatCodes", formatCodes);
    }
}