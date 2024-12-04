package com.xtree.lottery.ui.rule.prepare;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Rule(name = "Format Data With Layout", description = "格式化数据格式（有布局）")
public class FormatDataWithLayoutRule {

    @Priority
    public int getPriority() {
        return 90000;
    }

    @Condition
    public boolean when(Map<String, Object> facts) {
        return "ssc".equals(facts.get("lotteryType")) && ((Map<?, ?>) facts.get("currentMethod")).containsKey("selectarea.layout");
    }

    @Action
    public void then(Map<String, Object> facts) {
        List<Object> formatCodes = new ArrayList<>();
        List<?> layout = (List<?>) ((Map<?, ?>) facts.get("currentMethod")).get("selectarea.layout");
        List<?> betCodes = (List<?>) ((Map<?, ?>) facts.get("bet")).get("codes");

        for (int i = 0; i < layout.size(); i++) {
            Map<?, ?> item = (Map<?, ?>) layout.get(i);
            int place = (int) item.get("place");
            formatCodes.addAll(place, (List<?>) betCodes.get(i));
        }

        facts.put("formatCodes", formatCodes);
    }
}
